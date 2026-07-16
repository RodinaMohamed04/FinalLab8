package Frontend;

import Backend.*;

import java.awt.BorderLayout;
import javax.swing.JButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class LessonQuizAverageChart extends javax.swing.JFrame {

    private javax.swing.JPanel chartPanel;
    private javax.swing.JButton backButton;

    Instructor instructor;

    public LessonQuizAverageChart(Instructor instructor) {
        this.instructor = instructor;
        initComponents();
        createChart();
        setLocationRelativeTo(null);
    }

    private void createChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CourseService cs = new CourseService();
        UserService us = new UserService();

        for (String courseId : instructor.getCreatedCourses()) {
            Course c = cs.getCourseById(courseId);
            if (c == null) continue;

            for (Lesson l : c.getLessons()) {
                if (l.getQuiz() == null) continue;

                double totalScore = 0;
                int count = 0;

                for (int studentId : c.getStudents().stream().map(Student::getUserId).toList()) {
                    Student s = (Student) us.getUserbyID(studentId);
                    if (s == null) continue;

                    for (StudentQuizAttempt a : s.getQuizAttempts()) {
                        if (a.getQuizId().equalsIgnoreCase(l.getQuiz().getQuizId())) {
                            totalScore += a.getScore();
                            count++;
                        }
                    }
                }

                double avg = count == 0 ? 0 : totalScore / count;
                dataset.addValue(avg, "Average Score", l.getTitle());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Quiz Average per Lesson",
                "Lesson",
                "Average Score",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel cp = new ChartPanel(chart);
        chartPanel.setLayout(new BorderLayout());
        chartPanel.add(cp, BorderLayout.CENTER);
        chartPanel.validate();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        chartPanel = new javax.swing.JPanel();
        backButton = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        backButton.setText("Go Back");
        backButton.addActionListener(evt -> {
            InstructorDashBoard dashboard = new InstructorDashBoard(instructor);
            dashboard.setVisible(true);
            this.setVisible(false);
        });

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
                chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 328, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
                chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 288, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(chartPanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(34, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(backButton)
                                        .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chartPanel,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(backButton)
                                .addContainerGap())
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // new LessonQuizAverageChart(instructor).setVisible(true);
        });
    }
}