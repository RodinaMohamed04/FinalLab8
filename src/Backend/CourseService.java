package Backend;
import java.util.ArrayList;
public class CourseService {
    private ArrayList<Course> courses;
    private UserService userService;
    public CourseService() {
        userService = new UserService();
        courses = JsonDataBaseManager.readCourse();
    }
    private void saveCourses() {
        
        JsonDataBaseManager.saveCourse(courses);
        if (courses == null) courses = new ArrayList<>();
    }
    
    private void saveChanges(){
        JsonDataBaseManager.saveCourse(courses);
    }

    public void addCourse(Course c) {
        courses.add(c);
        saveCourses();
    }
    public Course getCourseById(String courseId) {
        for (Course c : courses) {
            if (c.getCourseId().equals(courseId)) {
                return c;
            }
        }
        return null;
    }
    public ArrayList<Course> getAllCourses() {
        return courses;
    }

    public boolean removeCourse(String courseId) {
        Course c = getCourseById(courseId);
        if (c != null) {

            // 1) Remove from courses list
            courses.remove(c);
            saveCourses();

            // 2) Remove from all users
            UserService us = new UserService();

            for (User u : us.getAllUsers()) {

                // Remove from instructor created courses
                if (u instanceof Instructor inst) {
                    inst.getCreatedCourses().remove(courseId);
                }

                // Remove from student enrolled courses + completed lessons
                if (u instanceof Student st) {
                    st.getEnrolledCourses().remove(courseId);

                }

            }
            us.saveUsers();
            return true;
        }
        return false;
    }

    public void updateCourse(String oldCourseId, String newName, String newCourseId) {
    Course c = getCourseById(oldCourseId);
    if (c != null) {
        c.setCourseId(newCourseId);     
        c.setCourseName(newName);
        saveCourses();
    }
}

    public  void addCourse(Course c,ArrayList<Lesson> lessons,ArrayList<Student> students){
        c.setLessons(lessons);
        c.setStudents(students);
        courses.add(c);
        saveCourses();
    }
    public ArrayList<Course> displayCourses() {
        return new ArrayList<>(courses);
}
    public void addLessonToCourse(String courseId, Lesson lesson) {
        Course c = getCourseById(courseId);
        if (c != null) {
            c.getLessons().add(lesson);
            saveCourses();
        }
    }

    public void removeLessonFromCourse(String courseId, String lessonId) {
        Course c = getCourseById(courseId);
        if (c != null) {
            c.getLessons().removeIf(l -> l.getLessonId().equals(lessonId));
            userService.removeLessonFromAllStudents(courseId, lessonId);
            saveCourses();
        }
    }
    public void updateLesson(String courseId, String oldLessonId, String newLessonId, String newTitle, String newContent, ArrayList<String> newResources) {
    Course c = getCourseById(courseId);
    if (c != null) {
        for (Lesson l : c.getLessons()) {
            if (l.getLessonId().equals(oldLessonId)) {
                l.setLessonId(newLessonId);
                l.setTitle(newTitle);
                l.setContent(newContent);
                l.getResources().clear();
                if (newResources != null) {
                    l.getResources().addAll(newResources); 
                }

                for (User u : userService.getAllUsers()) {
                    if (u instanceof Student s) {
                        for (StudentCourseProgress p : s.getCoursesProgress()) {
                            if (p.getCourseId().equals(courseId)) {
                                ArrayList<String> lessons = p.getCompletedLessons();
                                for (int i = 0; i < lessons.size(); i++) {
                                    if (lessons.get(i).equals(oldLessonId)) {
                                        lessons.set(i, newLessonId);
                                    }
                                }
                            }
                        }
                    }
                }
                userService.saveUsers();
                saveCourses();
                break;
            }
        }
    }
}
    public void addStudentToCourse(String courseId, Student student) {
        Course c = getCourseById(courseId);
        if (c != null && !c.getStudents().contains(student)) {
            c.getStudents().add(student);
            saveCourses();
        }
    }

    public void removeStudentFromCourse(String courseId, int studentId) {
        Course c = getCourseById(courseId);
        if (c != null) {
            c.getStudents().removeIf(s -> s.getUserId() == studentId);
            saveCourses();
        }
    }
    public ArrayList<Course> getInstructorCourses(int instructorId) {
    ArrayList<Course> result = new ArrayList<>();
    for (Course c : courses) {
        if (c.getInstructorId() == instructorId) {
            result.add(c);
        }
    }
    return result;
}
    public ArrayList<Lesson> displayLessons(String courseId) {
    Course c = getCourseById(courseId);
    if (c != null) {
        return new ArrayList<>(c.getLessons()); 
    } else {
        return new ArrayList<>(); 
    }
}
    public Lesson getLessonById(String courseId, String lessonId) {
        Course c = getCourseById(courseId);
        if (c != null) {
            for (Lesson l : c.getLessons()) {
                if (l.getLessonId().equals(lessonId)) {
                    return l;
                }
            }
        }
        return null;
    }

   
}

