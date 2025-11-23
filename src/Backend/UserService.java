package Backend;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.time.LocalDate;
public class UserService {
    private ArrayList<User> users;

    public UserService() {
        users = JsonDataBaseManager.read();
        int maxId = 100;
        for (User u : users) {
            if (u.getUserId() > maxId) {
                maxId = u.getUserId();
            }
        }
        User.setCounter(maxId + 1);
    }

    public void saveUsers() {
        JsonDataBaseManager.save(users);
    }

    public void addUser(User u) {
        users.add(u);
        saveUsers();
    }

    public User getUserbyID(int id) {
        for (User u : users) {
            if (u.getUserId() == id) {
                return u;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public void enrollStudent(int id, String courseID) {
        User u = getUserbyID(id);
        if (u instanceof Student s) {
            if (s.getEnrolledCourses().contains(courseID)) {
                JOptionPane.showMessageDialog(null,
                        "You already enrolled in this course!",
                        "Enrollment Error",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                s.addCourse(courseID);
                saveUsers();
                JOptionPane.showMessageDialog(null,
                        "Course enrolled successfully!",
                        "Enrollment",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void markLessonCompleted(int id, String courseID, String lessonID) {
        User u = getUserbyID(id);
        if (u instanceof Student s) {
            s.addLessonCompleted(courseID, lessonID);

            // check if student completed their course
            CourseService cs = new CourseService();
            Course c = cs.getCourseById(courseID);
            if (c != null) {
                int totalLessons = c.getLessons().size();
                int completedLessons = 0;
                for (StudentCourseProgress p : s.getCoursesProgress()) {
                    if (p.getCourseId().equals(courseID)) {
                        completedLessons = p.getCompletedLessons().size();
                        break;
                    }
                }

                if (completedLessons == totalLessons) {
                    // load certificate
                    boolean alreadyHasCert = false;
                    for (Certificate cert : s.getCertificates()) {
                        if (cert.getCourseId().equals(courseID)) {
                            alreadyHasCert = true;
                            break;
                        }
                    }

                    if (!alreadyHasCert) {
                        String certId = "CERT-" + System.currentTimeMillis();
                        Certificate newCert = new Certificate(certId, s.getUserId(), courseID, LocalDate.now().toString());
                        s.addCertificate(newCert);
                        System.out.println("Certificate generated for student " + s.getUserName() + " for course " + courseID);
                    }
                }
            }

            saveUsers();
        }
    }

    public void addCourseToInstructor(int instructorId, String courseId) {
        User u = getUserbyID(instructorId);
        if (u instanceof Instructor i) {
            i.addCreatedCourseId(courseId);
            saveUsers();
        }
    }

    public ArrayList<String> displayEnrolledCourses(int Id) {
        User u = getUserbyID(Id);
        if (u instanceof Student s) {
            return s.getEnrolledCourses();
        }
        return new ArrayList<>();
    }

    public void removeLessonFromAllStudents(String courseId, String lessonId) {
        for (User u : getAllUsers()) {
            if (u instanceof Student s) {
                for (StudentCourseProgress p : s.getCoursesProgress()) {
                    if (p.getCourseId().equals(courseId)) {
                        p.getCompletedLessons().remove(lessonId); // remove if exists
                    }
                }
            }
        }
        saveUsers();
    }

    public void updateCourseIdForAllUsers(String oldId, String newId) {
        for (User u : users) {

            if (u instanceof Instructor inst) {
                ArrayList<String> list = inst.getCreatedCourses();
                if (list.contains(oldId)) {
                    list.remove(oldId);
                    list.add(newId);
                }
            }

            if (u instanceof Student st) {
                ArrayList<String> list = st.getEnrolledCourses();
                if (list.contains(oldId)) {
                    list.remove(oldId);
                    list.add(newId);
                }

                ArrayList<StudentCourseProgress> progressList = st.getCoursesProgress();

                for (StudentCourseProgress p : progressList) {
                    if (p.getCourseId().equals(oldId)) {
                        p.setCourseId(newId);
                    }

                    //st.updateLessonMapCourseId(oldId, newId);
                }
            }

            saveUsers();
        }


    }
}
