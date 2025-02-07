package org.example.lmsproject.reportExcel.service;

import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.Plot;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ChartService {
    public byte[] generatePerformanceChart(List<StudentPerformance> performanceList) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (StudentPerformance performance : performanceList) {
            dataset.addValue(performance.getQuizGradePercentage(), performance.getUsername(), "Quiz Grade");
            dataset.addValue(performance.getAttendancePercentage(), performance.getUsername(), "Attendance Percentage");
            dataset.addValue(performance.getAssignmentScorePercentage(), performance.getUsername(), "Assignment Score");
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Student Performance",
                "Student",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return createChartImage(barChart);
    }

    public byte[] generateCourseCompletionChart(List<StudentPerformance> performanceList) throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        int totalStudents = performanceList.size();
        int completed = 0;
        for (StudentPerformance performance : performanceList) {
            double attendancePercentage = performance.getAttendancePercentage();
            if (attendancePercentage >= 75) {
                completed++;
            }
        }
        int incomplete = totalStudents - completed;
        dataset.setValue("Completed", completed);
        dataset.setValue("Incomplete", incomplete);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Course Completion", // Chart title
                dataset
        );

        return createChartImage(pieChart);
    }


    private byte[] createChartImage(JFreeChart chart) throws Exception {
        BufferedImage chartImage = chart.createBufferedImage(800, 600);
        ByteArrayOutputStream image = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", image);
        return image.toByteArray();
    }

}
