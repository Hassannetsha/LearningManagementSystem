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
import org.example.lmsproject.reportExcel.service.ChartService;
import org.example.lmsproject.reportExcel.service.PerformanceTrackingService;
import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/instructor/performance")
public class AnalyticsController {


    private final StudentRepository studentRepository;
    private final ChartService chartService;
    private final PerformanceTrackingService performanceTrackingService;
    private final CourseRepository courseRepository;

    @Autowired
    public AnalyticsController(StudentRepository studentRepository, ChartService chartService, PerformanceTrackingService performanceTrackingService, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.chartService = chartService;
        this.performanceTrackingService = performanceTrackingService;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentPerformance> getStudentPerformance(@PathVariable Long studentId) {
        if (studentRepository.findById(studentId).isPresent()) {
            StudentPerformance performance = performanceTrackingService.getPerformanceForStudent(studentId);
            return ResponseEntity.ok(performance);  // Returns 200 OK with student performance data
        } else {
            return ResponseEntity.status(404).body(null);  // You can also return a custom error message here
        }
    }


    @GetMapping("/generate-report/{courseId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable Long courseId) throws IOException {
        List<Student> students = courseRepository.findById(courseId).map(Course::getStudents).orElse(null);
        if (students == null || students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("No students found for the given course.".getBytes());  // Return a custom error message
        }
        byte[] excelReport = performanceTrackingService.generateStudentPerformanceReportForCourse(courseId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=student_performance_report.xlsx")
                .body(excelReport);
    }


    @PostMapping("/performance-bar/{courseId}")
    public ResponseEntity<byte[]> generatePerformanceBarChart(@PathVariable Long courseId) throws Exception {
        List<Student> students = courseRepository.findById(courseId).map(Course::getStudents).orElse(null);
        if (students == null || students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No students found for the given course.".getBytes());
        }
        List<StudentPerformance> performanceList = performanceTrackingService.getPerformanceForStudents(students);
        byte[] chartImage = chartService.generatePerformanceChart(performanceList);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"performance_bar_chart.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(chartImage);
    }

    @PostMapping("/course-completion-pie/{courseId}")
    public ResponseEntity<byte[]> generateCourseCompletionPieChart(@PathVariable Long courseId) throws Exception {
        List<Student> students;
        students=courseRepository.findById(courseId).map(Course::getStudents).orElse(null);
        if (students == null || students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No students found for the given course.".getBytes());
        }
        List<StudentPerformance> performanceList = performanceTrackingService.getPerformanceForStudents(students);
        byte[] chartImage = chartService.generateCourseCompletionChart(performanceList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"course_completion_pie_chart.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(chartImage);
    }

    @PostMapping("/progress-radar/{courseId}")
    public ResponseEntity<byte[]> generateProgressRadarChart(@PathVariable Long courseId) throws Exception {
        List<Student> students = courseRepository.findById(courseId).map(Course::getStudents).orElse(null);
        if (students == null || students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No students found for the given course.".getBytes());  // Return a custom error message

        }
        List<StudentPerformance> performanceList = performanceTrackingService.getPerformanceForStudents(students);
        byte[] chartImage = chartService.generateProgressChart(performanceList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"progress_radar_chart.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(chartImage);
    }
}