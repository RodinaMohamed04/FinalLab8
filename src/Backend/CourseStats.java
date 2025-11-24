/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import java.util.ArrayList;

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
    public ArrayList<CourseStatistics> getCourseStatistics() {
        ArrayList<CourseStatistics> statsList = new ArrayList<>();
        for (Course c : courseService.getAllCourses()) {
            if (!"Approved".equalsIgnoreCase(c.getStatus())) continue;

            int totalLessons = c.getLessons().size();
            int totalStudents = c.getStudents().size();
            double totalScore = 0;
            int totalAttempts = 0;
            int completedLessonsCount = 0;

            for (Student s : c.getStudents()) {

                StudentCourseProgress progress = s.getProgressForCourse(c.getCourseId());
                if (progress != null) {
                    completedLessonsCount += progress.getCompletedLessons().size();
                }


                for (StudentQuizAttempt a : s.getQuizAttempts()) {
                    Quiz q = null;
                    for (Lesson l : c.getLessons()) {
                        if (l.getQuiz() != null && l.getQuiz().getQuizId().equals(a.getQuizId())) {
                            q = l.getQuiz();
                            break;
                        }
                    }
                    if (q != null) {
                        totalScore += a.getScore();
                        totalAttempts++;
                    }
                }
            }

            double avgScore = totalAttempts == 0 ? 0 : totalScore / totalAttempts;
            double completionPercentage = (totalStudents * totalLessons == 0) ? 0 :
                    (double) completedLessonsCount / (totalStudents * totalLessons) * 100;

            statsList.add(new CourseStatistics(c.getCourseId(), c.getCourseName(), avgScore, completionPercentage, totalStudents));
        }
        return statsList;
    }
    public static class CourseStatistics {
        public String courseId;
        public String courseName;
        public double averageScore;
        public double completionPercentage;
        public int totalStudents;

        public CourseStatistics(String courseId, String courseName, double avg, double comp, int students) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.averageScore = avg;
            this.completionPercentage = comp;
            this.totalStudents = students;
        }
    }
}
