package Backend;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonDataBaseManager {

    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";

    /// for users (save and read)
    public static void save(ArrayList<User> x) {
        JSONArray arr = new JSONArray();
        for (User u : x) {
            JSONObject j = new JSONObject();
            j.put("Name", u.getUserName());
            j.put("ID", u.getUserId());
            j.put("Email", u.getEmail());
            j.put("Hash password", u.getPasswordHash());
            j.put("Role", u.getRole());

            if (u instanceof Student) {
                Student s = (Student) u;

                // enrolled courses
                JSONArray enrolled = new JSONArray();
                for (String c : s.getEnrolledCourses()) {
                    enrolled.put(c);
                }
                j.put("enrolledCourses", enrolled);

                // progress
                JSONArray progress = new JSONArray();
                for (StudentCourseProgress p : s.getCoursesProgress()) {
                    JSONObject pr = new JSONObject();
                    pr.put("courseId", p.courseId);
                    pr.put("completedLessons", new JSONArray(p.completedLessons));
                    progress.put(pr);
                }
                j.put("progress", progress);
                JSONArray attemptsArr = new JSONArray();
                for (StudentQuizAttempt a : s.getQuizAttempts()) {
                    JSONObject jAttempt = new JSONObject();
                    jAttempt.put("quizId", a.getQuizId());
                    jAttempt.put("answers", new JSONArray(a.getAnswers()));
                    jAttempt.put("score", a.getScore());
                    jAttempt.put("isPassed", a.isPassed());
                    attemptsArr.put(jAttempt);
                }
                j.put("quizAttempts", attemptsArr);
                //certificates
                JSONArray certs = new JSONArray();
                for (Certificate cc : s.getCertificates()) {
                    JSONObject jc = new JSONObject();
                    jc.put("certificateId", cc.getCertificateId());
                    jc.put("studentId", cc.getStudentId());
                    jc.put("courseId", cc.getCourseId());
                    jc.put("issueDate", cc.getIssueDate());
                    certs.put(jc);
                }
                j.put("certificates", certs);

            } else if (u instanceof Instructor) {
                Instructor i = (Instructor) u;

                JSONArray created = new JSONArray();
                for (String cId : i.getCreatedCourses()) {
                    created.put(cId);
                }
                j.put("createdCourses", created);
            }
            arr.put(j);

        }
        writeToFile(USERS_FILE, arr);
    }

    public static ArrayList<User> read() {
        ArrayList<User> users = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(USERS_FILE))) {
                return users;
            }

            String json = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject j = arr.getJSONObject(i);

                int id = j.getInt("ID");
                String name = j.getString("Name");
                String email = j.getString("Email");
                String pass = j.getString("Hash password");
                String role = j.getString("Role");

                // ---------- Student ----------
                /*if (role.equalsIgnoreCase("Student")) {
                    Student s = new Student(name, id, email, pass);

                    // load enrolled courses
                    JSONArray enrolled = j.getJSONArray("enrolledCourses");
                    for (int k = 0; k < enrolled.length(); k++) {
                        s.addCourse(enrolled.getString(k));
                    }

                    // load progress
                   JSONArray progress = j.getJSONArray("progress");
                    for (int k = 0; k < progress.length(); k++) {
                        JSONObject pr = progress.getJSONObject(k);
                        String courseId = pr.getString("courseId");

                        ArrayList<String> completed = new ArrayList<>();
                        JSONArray c = pr.getJSONArray("completedLessons");
                        for (int t = 0; t < c.length(); t++) {
                            completed.add(c.getString(t));
                        }

                        s.addProgress(courseId, completed);
                    }

                    users.add(s);
                }*/
                if (role.equalsIgnoreCase("Student")) {
                    Student s = new Student(name, id, email, pass);

                    // 1) Load enrolledCourses ONLY (without creating empty progress)
                    JSONArray enrolled = j.optJSONArray("enrolledCourses");
                    if (enrolled != null) {
                        ArrayList<String> enrolledList = new ArrayList<>();
                        for (int k = 0; k < enrolled.length(); k++) {
                            enrolledList.add(enrolled.getString(k));
                        }
                        s.setEnrolledCourses(enrolledList);
                    }

                    // 2) Load progress (actual data from JSON)
                    JSONArray progress = j.optJSONArray("progress");
                    if (progress != null) {
                        for (int k = 0; k < progress.length(); k++) {
                            JSONObject pr = progress.getJSONObject(k);

                            String courseId = pr.getString("courseId");

                            ArrayList<String> completed = new ArrayList<>();
                            JSONArray c = pr.getJSONArray("completedLessons");
                            for (int t = 0; t < c.length(); t++) {
                                completed.add(c.getString(t));
                            }

                            s.addProgress(courseId, completed);
                        }
                    }
                    // Load quiz attempts
                    JSONArray attemptsArr = j.optJSONArray("quizAttempts");
                    if (attemptsArr != null) {
                        for (int k = 0; k < attemptsArr.length(); k++) {
                            JSONObject a = attemptsArr.getJSONObject(k);
                            StudentQuizAttempt attempt = new StudentQuizAttempt(String.valueOf(id),
                                    a.getString("quizId"));
                            JSONArray answers = a.getJSONArray("answers");
                            for (int t = 0; t < answers.length(); t++)
                                attempt.getAnswers().add(answers.getString(t));
                            attempt.setScore(a.getInt("score"));
                            attempt.setPassed(a.getBoolean("isPassed"));
                            s.addQuizAttempt(attempt);
                        }
                    }
                    // 3) Load certificates (from user.json file)
                    JSONArray certs = j.optJSONArray("certificates");
                    if (certs != null) {
                        for (int k = 0; k < certs.length(); k++) {
                            JSONObject jc = certs.getJSONObject(k);

                            String certId = jc.getString("certificateId");
                            int studentId = jc.getInt("studentId");
                            String courseId = jc.getString("courseId");
                            String issueDate = jc.getString("issueDate");

                            Certificate cObj = new Certificate(certId, studentId, courseId, issueDate);
                            s.addCertificate(cObj);
                        }
                    }

                    users.add(s);
                } // ---------- Instructor ----------
                else if (role.equalsIgnoreCase("Instructor")) {
                    Instructor ins = new Instructor(name, id, email, pass);

                    JSONArray created = j.getJSONArray("createdCourses");
                    for (int k = 0; k < created.length(); k++) {
                        ins.addCreatedCourseId(created.getString(k));
                    }

                    users.add(ins);
                } // ---------- Admin ----------
                else if (role.equalsIgnoreCase("Admin")) {
                    Admin admin = new Admin(name, id, email, pass);

                    users.add(admin);
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading users.json");
        }

        return users;
    }

    // ==================== Helper ====================
    private static void writeToFile(String filename, JSONArray arr) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(arr.toString(4)); // pretty
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }

    /// /// courses and lessons save and read
    public static void saveCourse(ArrayList<Course> courses) {
        JSONArray arr = new JSONArray();
        for (Course c : courses) {
            JSONObject j = new JSONObject();

            j.put("courseId", c.getCourseId());
            j.put("courseName", c.getCourseName());
            j.put("courseDescription", c.getCourseDescription());
            j.put("InstructorId", c.getInstructorId());
            j.put("status", c.getStatus());

            JSONArray studentArr = new JSONArray();
            for (Student s : c.getStudents()) {
                studentArr.put(s.getUserId());

            }
            j.put("students", studentArr);

            JSONArray lessonArr = new JSONArray();
            for (Lesson l : c.getLessons()) {
                JSONObject jl = new JSONObject();
                jl.put("lessonId", l.getLessonId());
                jl.put("title", l.getTitle());
                jl.put("content", l.getContent());

                JSONArray resArr = new JSONArray(l.getResources());
                jl.put("resources", resArr);


                if (l.getQuiz() != null) {
                    JSONObject jq = new JSONObject();
                    jq.put("quizId", l.getQuiz().getQuizId());
                    jq.put("passPercentage", l.getQuiz().getPassPercentage());
                    JSONArray questionsArr = new JSONArray();
                    for (Question q : l.getQuiz().getQuestions()) {
                        JSONObject qj = new JSONObject();
                        qj.put("questionText", q.getQuestionText());
                        qj.put("options", new JSONArray(q.getOptions()));
                        qj.put("correctAnswer", q.getCorrectAnswer());
                        questionsArr.put(qj);
                    }
                    jq.put("questions", questionsArr);
                    jl.put("quiz", jq);
                }
                lessonArr.put(jl);
            }
                j.put("lessons", lessonArr);
                arr.put(j);
            }

            writeToFile(COURSES_FILE, arr);
        }


    public static ArrayList<Course> readCourse() {
        ArrayList<Course> courses = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(COURSES_FILE))) {
                return courses;
            }

            String json = new String(Files.readAllBytes(Paths.get(COURSES_FILE)));
            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject j = arr.getJSONObject(i);

                String courseId = j.getString("courseId");
                String courseName = j.getString("courseName");
                String courseDescription = j.getString("courseDescription");
                int instructorId = j.getInt("InstructorId");
                String status = j.optString("status", "PENDING");

                Course c = new Course(courseId, courseName, courseDescription, instructorId);
                c.setStatus(status);

                JSONArray studentArr = j.getJSONArray("students");

                for (int k = 0; k < studentArr.length(); k++) {
                    int studentId = studentArr.getInt(k);

                    c.getStudents().add(new Student("temp", studentId, "temp", "temp"));

                }
                JSONArray lessonArr = j.getJSONArray("lessons");
                for (int k = 0; k < lessonArr.length(); k++) {
                    JSONObject jl = lessonArr.getJSONObject(k);

                    Lesson l = new Lesson(jl.getString("lessonId"), jl.getString("title"), jl.getString("content"));

                    JSONArray resArr = jl.getJSONArray("resources");
                    for (int t = 0; t < resArr.length(); t++) {
                        l.getResources().add(resArr.getString(t));
                    }
                    if (jl.has("quiz")) {
                        JSONObject jq = jl.getJSONObject("quiz");
                        Quiz quiz = new Quiz(jq.getString("quizId"), l.getLessonId(),
                                new ArrayList<>(), jq.getDouble("passPercentage"));
                        JSONArray questionsArr = jq.optJSONArray("questions");
                        if (questionsArr != null) {
                            for (int t = 0; t < questionsArr.length(); t++) {
                                JSONObject qj = questionsArr.getJSONObject(t);
                                Question q = new Question(
                                        qj.getString("questionText"),
                                        jsonArrayToStringArray(qj.getJSONArray("options")),
                                        qj.getString("correctAnswer"));
                                quiz.getQuestions().add(q);
                            }
                        }
                        l.setQuiz(quiz);
                    }

                    c.getLessons().add(l);
                }

                courses.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error reading courses.json");
        }
        return courses;
    }
    private static String[] jsonArrayToStringArray(JSONArray arr) {
        String[] result = new String[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            result[i] = arr.getString(i);
        }
        return result;
    }
}