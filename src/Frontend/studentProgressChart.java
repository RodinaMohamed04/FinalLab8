package Frontend;

import Backend.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class studentProgressChart extends javax.swing.JFrame {

    private javax.swing.JPanel chartPanel;
    private javax.swing.JButton backButton;

    Instructor instructor;

    public studentProgressChart(Instructor instructor) {
        this.instructor = instructor;
        initComponents();
        createProgressChart();
        setLocationRelativeTo(null);
    }

    private void createProgressChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CourseService cs = new CourseService();
        UserService us = new UserService();

        for (String courseId : instructor.getCreatedCourses()) {
            Course course = cs.getCourseById(courseId);
            if (course == null) continue;

            ArrayList<Student> students = new ArrayList<>();

            for (User u : us.getAllUsers()) {
                if (u instanceof Student s) {
                    if (s.getEnrolledCourses().contains(courseId)) {
                        students.add(s);
                    }
                }
            }

            int totalLessons = course.getLessons().size();
            double avgCompletion = 0;

            if (!students.isEmpty() && totalLessons > 0) {
                int completedTotal = 0;

                for (Student s : students) {
                    StudentCourseProgress progress = s.getProgressForCourse(courseId);
                    if (progress != null) {
                        completedTotal += progress.getCompletedLessons().size();
                    }
                }

                avgCompletion = (double) completedTotal
                        / (students.size() * totalLessons) * 100;
            }

            dataset.addValue(avgCompletion, "Progress %", course.getCourseName());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Student Progress per Course",
                "Course",
                "Progress (%)",
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
                        .addGap(0, 379, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
                chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 214, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chartPanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(backButton)
                                        .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(chartPanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(backButton)
                                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // new studentProgressChart(instructor).setVisible(true);
        });
    }
}