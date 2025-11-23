package Backend;

import java.util.ArrayList;

public class QuizService {

    private ArrayList<StudentQuizAttempt> attempts;
    private CourseService courseService = new CourseService();

    public QuizService() {
        this.attempts = new ArrayList<>();
    }

    // Start new attempt
    public StudentQuizAttempt startAttempt(Student s, Quiz q) {
        StudentQuizAttempt attempt = new StudentQuizAttempt(String.valueOf(s.getUserId()), q.getQuizId());
        attempts.add(attempt);
        return attempt;
    }

    // Submit answer
    public void submitAnswer(StudentQuizAttempt attempt, int questionIndex, String answer) {
        ArrayList<String> answers = attempt.getAnswers();

        while (answers.size() <= questionIndex) {
            answers.add("");
        }

        answers.set(questionIndex, answer);
    }

    // Calculate score
    public void calculateScore(Student student, StudentQuizAttempt attempt, Quiz quiz) {
        int score = 0;
        ArrayList<String> answers = attempt.getAnswers();
        ArrayList<Question> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            if (i < answers.size() && questions.get(i).getCorrectAnswer().equals(answers.get(i))) {
                score++;
            }
        }

        attempt.setScore(score);
        attempt.setPassed(quiz.isPassed(score));

        if (attempt.isPassed()) {
            Course course = courseService.getCourseByLessonId(quiz.getLessonId());
            if (course != null) {
               /* String courseId = course.getCourseId();
                String lessonId = quiz.getLessonId();
                student.addLessonCompleted(courseId, lessonId);*/
                student.addLessonCompleted(course.courseId, quiz.getLessonId());
            }        }
    }

    // Get all attempts for a student
    public ArrayList<StudentQuizAttempt> getAttempts(String studentId) {
        ArrayList<StudentQuizAttempt> result = new ArrayList<>();
        for (StudentQuizAttempt a : attempts) {
            if (a.getStudentId().equals(studentId)) {
                result.add(a);
            }
        }
        return result;
    }

    // Save attempt inside Student object
    public void saveAttempt(Student s, StudentQuizAttempt attempt) {
        s.addQuizAttempt(attempt);
    }

    // Check if student can access a lesson
    public boolean canAccessLesson(Student student, Course course, Lesson lesson) {
        int index = course.getLessons().indexOf(lesson);

        if (index == 0)
            return true;

        Lesson previousLesson = course.getLessons().get(index - 1);
        String courseId = course.getCourseId();
        String prevLessonId = previousLesson.getLessonId();

        return student.isLessonCompleted(courseId, prevLessonId);
    }
    
    
      public void addQuizToLesson(String courseId, String lessonId, Quiz quiz) {
        Lesson lesson = courseService.getLessonById(courseId, lessonId);
        if (lesson != null) {
            lesson.setQuiz(quiz);
            courseService.saveCourses();
        }
    }

    public void addQuestionToQuiz(String courseId, String lessonId, Question question) {
        Lesson lesson = courseService.getLessonById(courseId, lessonId);
        if (lesson != null && lesson.getQuiz() != null) {
            lesson.getQuiz().getQuestions().add(question);
            courseService.saveCourses();
        }
    }

    public void removeQuestionFromQuiz(String courseId, String lessonId, int questionIndex) {
        Lesson lesson = courseService.getLessonById(courseId, lessonId);
        if (lesson != null && lesson.getQuiz() != null) {
            if (questionIndex >= 0 && questionIndex < lesson.getQuiz().getQuestions().size()) {
                lesson.getQuiz().getQuestions().remove(questionIndex);
                courseService.saveCourses();
            }
        }
    }

    public void updateQuestionInQuiz(String courseId, String lessonId, int questionIndex, Question newQuestion) {
        Lesson lesson = courseService.getLessonById(courseId, lessonId);
        if (lesson != null && lesson.getQuiz() != null) {
            if (questionIndex >= 0 && questionIndex < lesson.getQuiz().getQuestions().size()) {
                lesson.getQuiz().getQuestions().set(questionIndex, newQuestion);
                courseService.saveCourses();
            }
        }
    }
}
