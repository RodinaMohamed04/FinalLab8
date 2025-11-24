/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

/**
 *
 * @author mo
 */
public class CourseStats {
    private CourseService courseService;

    public CourseStats() {
        courseService = new CourseService();
    }

    public int getApprovedCount() {
        int count = 0;
        for (Course c : courseService.getAllCourses()) {
            if ("Approved".equalsIgnoreCase(c.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getPendingCount() {
        int count = 0;
        for (Course c : courseService.getAllCourses()) {
            if ("Pending".equalsIgnoreCase(c.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getRejectedCount() {
        int count = 0;
        for (Course c : courseService.getAllCourses()) {
            if ("Rejected".equalsIgnoreCase(c.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
