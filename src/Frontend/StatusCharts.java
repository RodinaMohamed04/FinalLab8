/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Frontend;

import Backend.Course;
import Backend.CourseService;
import Backend.CourseStats;
import Backend.Instructor;
import java.awt.BorderLayout;
import javax.swing.JButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author mo
 */
public class StatusCharts extends javax.swing.JFrame {

    private javax.swing.JPanel chartPanel;
    private javax.swing.JButton backButton;

    Instructor instructor;

    /**
     * Creates new form Charts
     */
    public StatusCharts(Instructor instructor) {
        this.instructor = instructor;
        initComponents();
        createChart();
        setLocationRelativeTo(null);
    }

    private void createChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CourseService cs = new CourseService();

        int approved = 0, pending = 0, rejected = 0;

        for (Course c : cs.getAllCourses()) {
            switch (c.getStatus()) {
                case "Approved" -> approved++;
                case "Pending" -> pending++;
                case "Rejected" -> rejected++;
            }
        }

        dataset.addValue(approved, "Courses", "Approved");
        dataset.addValue(pending, "Courses", "Pending");
        dataset.addValue(rejected, "Courses", "Rejected");

        JFreeChart chart = ChartFactory.createBarChart(
                "Courses Status Overview",
                "Status",
                "Count",
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
            InstructorDashBoard admin = new InstructorDashBoard(instructor);
            admin.setVisible(true);
            this.setVisible(false);
        });

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
                chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 342, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
                chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 254, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(chartPanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(29, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(backButton)
                                        .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(chartPanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(backButton)
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            // new StatusCharts(instructor).setVisible(true);
        });
    }
}