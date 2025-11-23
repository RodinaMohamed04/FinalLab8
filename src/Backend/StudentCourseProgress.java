package Backend;

import java.util.ArrayList;

// Class to track progress per course without Map
public class StudentCourseProgress {


    String courseId;
    ArrayList<String> completedLessons;

    public StudentCourseProgress(String courseId) {
        this.courseId = courseId;
        this.completedLessons = new ArrayList<>();
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCompletedLessons(ArrayList<String> completedLessons) {
        this.completedLessons = completedLessons;
    }
    public String getCourseId() {
        return courseId;
    }

    public ArrayList<String> getCompletedLessons() {
        return completedLessons;
    }

}


