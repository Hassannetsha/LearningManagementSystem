package org.example.lmsproject.reportExcel.controller;


import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.example.lmsproject.reportExcel.service.ChartService;
import org.example.lmsproject.reportExcel.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final ReportService reportService;
    private final ChartService chartService;

    @Autowired
    public AnalyticsController(ReportService reportService, ChartService chartService) {
        this.reportService = reportService;
        this.chartService = chartService;
    }

    @GetMapping("/generateReport")
    public ResponseEntity<byte[]> generateReport() throws Exception {
        List<StudentPerformance> performanceList = reportService.getStudentPerformance();
        byte[] reportData = reportService.generateStudentPerformanceReport(performanceList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(reportData);
    }


    // Endpoint to generate the performance chart
    @PostMapping("/generatePerformanceChart")
    public ResponseEntity<byte[]> generatePerformanceChart(@RequestBody List<StudentPerformance> performanceList) throws Exception {
        byte[] chartData = chartService.generatePerformanceChart(performanceList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance_chart.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(chartData);
    }
}
