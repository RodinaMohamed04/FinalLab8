package Backend;

import java.util.ArrayList;

public class Course {
    protected String courseId;
    protected String courseName;
    protected String courseDescription;
    protected int instructorId;
    protected ArrayList<Student> students;
    protected ArrayList<Lesson> lessons;

    public Course(String courseId, String courseName, String courseDescription, int instructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    public String getCourseId() {
        return courseId;
    }
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseDescription() {
        return courseDescription;
    }
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
    public int getInstructorId() {
        return instructorId;}
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;}
    public  ArrayList<Student> getStudents() {
        return students;
    }
    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }
}