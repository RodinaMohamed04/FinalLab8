package Backend;

import java.util.ArrayList;

public class StudentQuizAttempt {
    private String studentId;
    private String quizId;
    private ArrayList<String> answers;
    private int score;
    private boolean isPassed;

    public StudentQuizAttempt(String studentId, String quizId) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.answers = new ArrayList<>();
        this.score = 0;
        this.isPassed = false;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getQuizId() {
        return quizId;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }
}
