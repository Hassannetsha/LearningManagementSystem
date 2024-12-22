package org.example.lmsproject.reportExcel.service;

import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import org.example.lmsproject.course.model.Attendance;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.AttendanceRepository;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.FeedBackRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizSubmissionRepository;
import org.example.lmsproject.quiz.model.Quiz.FeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizSubmission;
import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceTrackingService {

    private final AttendanceRepository attendanceRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final StudentRepository studentRepository;
    private final FeedBackRepository feedBackRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public PerformanceTrackingService(AttendanceRepository attendanceRepository,
                                      AssignmentSubmissionRepository assignmentSubmissionRepository,
                                      QuizSubmissionRepository quizSubmissionRepository,
                                      StudentRepository studentRepository,
                                      FeedBackRepository feedBackRepository, CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
        this.studentRepository = studentRepository;
        this.feedBackRepository = feedBackRepository;
        this.courseRepository = courseRepository;
    }

    public double calculateAttendancePercentage(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        long totalLessons = attendances.size();
        long attendedLessons = attendances.stream().filter(Attendance::isPresent).count();
        return (double) attendedLessons / totalLessons * 100;
    }


    public double calculateQuizGrade(Long studentId) {
        List<FeedBack> allQuizzes = feedBackRepository.findByStudentId(studentId);
        double totalGrade = 0;
        for (FeedBack quizFeedback : allQuizzes) {
            totalGrade += quizFeedback.getGrade();
        }
        return totalGrade / allQuizzes.size(); // Average grade
    }

    public double calculateAssignmentScore(Long studentId) {
        List<AssignmentSubmission> submissions = assignmentSubmissionRepository.findByStudentId(studentId);
        double totalScore = 0;
        for (AssignmentSubmission submission : submissions) {
            totalScore += submission.getGrade();
        }
        return totalScore / submissions.size();  // Average score
    }

    //per student
    public StudentPerformance getPerformanceForStudent(Long studentId) {
        String username = studentRepository.findById(studentId).get().getUsername();
        double quizGrade = calculateQuizGrade(studentId);
        double attendancePercentage = calculateAttendancePercentage(studentId);
        double assignmentScore = calculateAssignmentScore(studentId);
        return new StudentPerformance(username, studentId, quizGrade, attendancePercentage, assignmentScore);
    }

    public byte[] generateStudentPerformanceReport(List<Long> studentIds) throws IOException {
        List<StudentPerformance> performanceList = new ArrayList<>();

        for (Long studentId : studentIds) {
            performanceList.add(getPerformanceForStudent(studentId));
        }

        // Generate the report (Excel)
        return generateExcelReport(performanceList);
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
            performanceList.add(getPerformanceForStudent(student.getId()));
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
            row.createCell(2).setCellValue(performance.getQuizGrade());
            row.createCell(3).setCellValue(performance.getAttendancePercentage());
            row.createCell(4).setCellValue(performance.getAssignmentScore());
        }
        // Write the output to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray();
    }


    //for all students
    public List<StudentPerformance> getPerformanceForStudents(List<Student> students) {
        List<StudentPerformance> performanceList = new ArrayList<>();
        for (Student student : students) {
            performanceList.add(getPerformanceForStudent(student.getId()));
        }
        return performanceList;
    }

}
