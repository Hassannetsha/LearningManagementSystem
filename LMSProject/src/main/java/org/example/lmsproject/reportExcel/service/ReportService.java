package org.example.lmsproject.reportExcel.service;

import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.AttendanceRepository;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.FeedBackRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizRepository;
import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;


@Service
public class ReportService {

    private final CourseRepository courseRepository;
    private final PerformanceTrackingService performanceTrackingService;

    public ReportService(AttendanceRepository attendanceRepository, AssignmentSubmissionRepository assignmentSubmissionRepository, StudentRepository studentRepository, FeedBackRepository feedBackRepository, CourseRepository courseRepository, PerformanceTrackingService quizRepository) {
        this.courseRepository = courseRepository;
        this.performanceTrackingService = quizRepository;
    }

    //for specific course
    public byte[] generateStudentPerformanceReportForCourse(Long courseId) throws IOException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        List<Student> students = course.getStudents();
        if (students.isEmpty()) {
            throw new IllegalArgumentException("No students found for the course");
        }
        List<StudentPerformance> performanceList = new ArrayList<>();
        for (Student student : students) {
            performanceList.add(performanceTrackingService.getPerformanceForStudent(student.getId()));
        }

        return generateExcelReport(performanceList);
    }


    private byte[] generateExcelReport(List<StudentPerformance> performanceList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Performance Report");
        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student ID");
        header.createCell(1).setCellValue("Username");
        header.createCell(2).setCellValue("Quiz Grade");
        header.createCell(3).setCellValue("Attendance Percentage");
        header.createCell(4).setCellValue("Assignment Score");
        // Populate rows with student performance data
        int rowIndex = 1;
        for (StudentPerformance performance : performanceList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(performance.getId());
            row.createCell(1).setCellValue(performance.getUsername());
            row.createCell(2).setCellValue(performance.getQuizGradePercentage());
            row.createCell(3).setCellValue(performance.getAttendancePercentage());
            row.createCell(4).setCellValue(performance.getAssignmentScorePercentage());
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray();
    }


    public byte[] generateStudentPerformanceReport(List<Long> studentIds) throws IOException {
        List<StudentPerformance> performanceList = new ArrayList<>();
        for (Long studentId : studentIds) {
            performanceList.add(performanceTrackingService.getPerformanceForStudent(studentId));
        }
        return generateExcelReport(performanceList);
    }
}