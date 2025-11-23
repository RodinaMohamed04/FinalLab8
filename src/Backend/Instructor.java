package Backend;

import java.util.ArrayList;

public class Instructor extends User{
    private ArrayList<String> createdCourses;
    public Instructor(String userName, int userId, String email, String passwordHash) {
        super(userName, userId, email, passwordHash, "instructor");
        this.createdCourses = new ArrayList<>();
    }
    public Instructor(String userName, String email, String passwordHash) {
        super(userName, email, passwordHash, "instructor");
        this.createdCourses = new ArrayList<>();
    }

    public void addCreatedCourseId(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }

        public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }
    public void setCreatedCourses(ArrayList<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

}
