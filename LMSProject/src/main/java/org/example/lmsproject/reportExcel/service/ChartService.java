package org.example.lmsproject.reportExcel.service;

import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ChartService {

    public byte[] generatePerformanceChart(List<StudentPerformance> performanceList) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Fill dataset with StudentPerformance data
        for (StudentPerformance performance : performanceList) {
            dataset.addValue(performance.getGrade(), performance.getStudentName(), performance.getCourse());
        }

        // Create chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Student Performance",
                "Course",
                "Grade",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Convert to image
        BufferedImage chartImage = barChart.createBufferedImage(800, 600);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);

        return baos.toByteArray();
    }
}

