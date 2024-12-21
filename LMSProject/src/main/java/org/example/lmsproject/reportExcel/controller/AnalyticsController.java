//package org.example.lmsproject.reportExcel.controller;
//
//
//import org.example.lmsproject.reportExcel.model.StudentPerformance;
//import org.example.lmsproject.reportExcel.service.ChartService;
//import org.example.lmsproject.reportExcel.service.ReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/analytics")
//public class AnalyticsController {
//
//    private final ReportService reportService;
//    private final ChartService chartService;
//
//    @Autowired
//    public AnalyticsController(ReportService reportService, ChartService chartService) {
//        this.reportService = reportService;
//        this.chartService = chartService;
//    }
//
//    @GetMapping("/generateReport")
//    public ResponseEntity<byte[]> generateReport() throws Exception {
//        List<StudentPerformance> performanceList = reportService.getStudentPerformance();
//        byte[] reportData = reportService.generateStudentPerformanceReport(performanceList);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance_report.xlsx")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(reportData);
//    }
//
//
//    // Endpoint to generate the performance chart
//    @PostMapping("/generatePerformanceChart")
//    public ResponseEntity<byte[]> generatePerformanceChart(@RequestBody List<StudentPerformance> performanceList) throws Exception {
//        byte[] chartData = chartService.generatePerformanceChart(performanceList);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance_chart.png")
//                .contentType(MediaType.IMAGE_PNG)
//                .body(chartData);
//    }
//}


package org.example.lmsproject.reportExcel.controller;

import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.Services.Quizzes.QuizServices;
import org.example.lmsproject.reportExcel.service.PerformanceTrackingService;
import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.example.lmsproject.userPart.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/instructor/performance")
public class AnalyticsController {

    private final PerformanceTrackingService performanceTrackingService;
    private final CourseRepository courseRepository;

    @Autowired
    public AnalyticsController(PerformanceTrackingService performanceTrackingService, CourseRepository courseRepository) {
        this.performanceTrackingService = performanceTrackingService;
        this.courseRepository = courseRepository;
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentPerformance> getStudentPerformance(@PathVariable Long studentId) {
        StudentPerformance performance = performanceTrackingService.getStudentPerformance(studentId);
        return ResponseEntity.ok(performance);
    }
    @GetMapping("/generate-report/{courseId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable Long courseId) throws IOException {
        List<Student> students = courseRepository.findById(courseId).map(Course::getStudents).orElse(null);
        if (students == null || students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Return an error if no students are found
        }
        byte[] excelReport = performanceTrackingService.generateStudentPerformanceReportForCourse(courseId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=student_performance_report.xlsx")
                .body(excelReport);
    }

}