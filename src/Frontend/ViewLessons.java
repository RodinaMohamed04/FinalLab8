package Frontend;

import Backend.*;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ViewLessons extends javax.swing.JFrame {

    private Student student;
    private Course course;
    private CourseService courseService;
    private String courseId;
    private Quiz quiz;
    private QuizService quizService;

    public ViewLessons(Student student, String courseId) {
        this.student = student;
        this.courseId = courseId;
        initComponents();
        this.setLocationRelativeTo(null);


        this.courseService = new CourseService();
        this.quizService = new QuizService();
        course = courseService.getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "Course not found!");
            return;
        }

        loadLessons();
    }

    private void loadLessons() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        ArrayList<Lesson> lessons = courseService.displayLessons(course.getCourseId());
        for (Lesson lesson : lessons) {
          /*  boolean completed = student.isLessonCompleted(course.getCourseId(), lesson.getLessonId());*/
            boolean completed = false;
            if (lesson.getQuiz() != null) {

                for (StudentQuizAttempt attempt : student.getQuizAttempts()) {
                    if (attempt.getQuizId().equals(lesson.getQuiz().getQuizId()) && attempt.isPassed()) {
                        completed = true;
                        break;
                    }
                }
            } else {

                completed = student.isLessonCompleted(course.getCourseId(), lesson.getLessonId());
            }

            model.addRow(new Object[]{
                    lesson.getLessonId(),
                    lesson.getTitle(),
                    completed ? "Yes" : "No"
            });
        }

        jTable1.setModel(model);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        startQuizBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("                         Lessons");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jButton1.setText("Go back");
        jButton1.addActionListener(evt -> {
            StudentDashBoard st = new StudentDashBoard(student);
            st.setVisible(true);
            this.setVisible(false);
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String[]{
                        "Lesson Id", "Lesson Title", "Status"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        startQuizBtn.setText("Start Quiz");
        startQuizBtn.addActionListener(evt -> startQuizBtnActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(66, 66, 66)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(195, 195, 195)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(226, 226, 226)
                                                .addComponent(jButton1)
                                                .addGap(107, 107, 107)
                                                .addComponent(startQuizBtn)))
                                .addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(startQuizBtn))
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }

    private void startQuizBtnActionPerformed() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson first!");
            return;
        }

        String lessonId = jTable1.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to start the quiz now?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Lesson lesson = courseService.getLessonById(course.getCourseId(), lessonId);
            if (lesson.getQuiz() != null) {
                quiz = lesson.getQuiz();
                QuizPage qp = new QuizPage(student,quiz,quizService);
              qp.setVisible(true);
               this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "No quiz for this lesson!");
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // Example: new ViewLessons(studentObj, "courseId").setVisible(true);
        });
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton startQuizBtn;
}
