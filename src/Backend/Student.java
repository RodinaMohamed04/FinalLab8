package Backend;

import java.util.ArrayList;

public class Student extends User {
    private ArrayList<String> enrolledCourses;
    private ArrayList<StudentCourseProgress> coursesProgress;
  private ArrayList<StudentQuizAttempt> attempts;

    public Student(String userName, int userId, String email, String passwordHash) {
        super(userName, userId, email, passwordHash, "student");
        this.enrolledCourses = new ArrayList<>();
        this.coursesProgress = new ArrayList<>();
        this.attempts = new ArrayList<>();
    }
    public Student(String userName, String email, String passwordHash) {
        super(userName, email, passwordHash, "student");
        this.enrolledCourses = new ArrayList<>();
        this.coursesProgress = new ArrayList<>();
        this.attempts = new ArrayList<>();
    }
    // Enroll student to a course
    public void addCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            coursesProgress.add(new StudentCourseProgress(courseId));
        }
    }

    // Add full progress (used by JSON loader)
    public void addProgress(String courseId, ArrayList<String> completedLessons) {

        StudentCourseProgress p = new StudentCourseProgress(courseId);
        p.setCompletedLessons(completedLessons);
        coursesProgress.add(p);
    }

    // Mark lesson as completed
   /* public void addLessonCompleted(String courseId, String lessonId) {
        for (StudentCourseProgress p : coursesProgress) {
            if (p.getCourseId().equals(courseId) && !p.getCompletedLessons().contains(lessonId)) {
                p.getCompletedLessons().add(lessonId);
                break;
            }
        }
    }*/

    /*public void addLessonCompleted(String courseId, String lessonId) {

        StudentCourseProgress target = null;
        for (StudentCourseProgress p : coursesProgress) {
            if (p.getCourseId().equals(courseId)) {
                target = p;
                break;
            }
        }


        if (target == null) {
            target = new StudentCourseProgress(courseId);
            coursesProgress.add(target);
        }

        if (!target.getCompletedLessons().contains(lessonId)) {
            target.getCompletedLessons().add(lessonId);
        }
    }*/
    public void addLessonCompleted(String courseId, String lessonId) {
        for (StudentCourseProgress p : coursesProgress) {
            if (p.getCourseId().equals(courseId)) {
                if (!p.getCompletedLessons().contains(lessonId)) {
                    p.getCompletedLessons().add(lessonId);
                }
                return;
            }
        }
        StudentCourseProgress newProgress = new StudentCourseProgress(courseId);
        newProgress.getCompletedLessons().add(lessonId);
        coursesProgress.add(newProgress);
    }



    public boolean isLessonCompleted(String courseId, String lessonId) {
    for (StudentCourseProgress p : coursesProgress) {
        if (p.getCourseId().equals(courseId) && p.getCompletedLessons().contains(lessonId)) {
            return true;
        }
    }
    return false;
}

   /* public int getProgressPercentage(String courseId, int totalLessons) {
        for (StudentCourseProgress p : coursesProgress) {
            if (p.getCourseId().equals(courseId)) {
                if (totalLessons == 0) return 0;
                return (p.getCompletedLessons().size() * 100 / totalLessons);
            }
        }
        return 0;
    }*/
   public int getProgressPercentage(String courseId, int totalLessons) {
       if (totalLessons == 0) return 0;
       for (StudentCourseProgress p : coursesProgress) {
           if (p.getCourseId().equals(courseId)) {
               return (p.getCompletedLessons().size() * 100 / totalLessons);
           }
       }
       return 0;
   }

    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public ArrayList<StudentCourseProgress> getCoursesProgress() {
        return coursesProgress;
    }
    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
    public void setCoursesProgress(ArrayList<StudentCourseProgress> coursesProgress) {
        this.coursesProgress = coursesProgress;
    }
    public void addQuizAttempt(StudentQuizAttempt attempt) {

        attempts.add(attempt);
    }
    public ArrayList<StudentQuizAttempt> getQuizAttempts() {
        return attempts;
    }

}