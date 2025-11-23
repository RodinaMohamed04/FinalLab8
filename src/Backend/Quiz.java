package Backend;

import java.util.ArrayList;

public class Quiz {
    private String quizId;
    private String lessonId;
    private ArrayList<Question> questions = new ArrayList<>();
    private double passPercentage;
    private int maxAttempts = 3;

    public Quiz(String quizId, String lessonId, ArrayList<Question> questions, double passPercentage) {
        this.quizId = quizId;
        this.lessonId = lessonId;
        this.passPercentage = passPercentage;
        this.questions = questions;
    }

    // Getters and Setters
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public double getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(double passPercentage) {
        this.passPercentage = passPercentage;
    }

    public boolean isPassed(int score) {
        double percentage = (score * 100.0) / questions.size();
        return percentage >= passPercentage;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
