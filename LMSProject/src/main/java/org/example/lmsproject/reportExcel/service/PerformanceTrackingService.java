package org.example.lmsproject.reportExcel.service;

import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import org.example.lmsproject.course.model.Attendance;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.AttendanceRepository;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.FeedBackRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizSubmissionRepository;
import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;
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
    }

    public double calculateAttendancePercentage(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        long totalLessons = attendances.size();
        long attendedLessons = attendances.stream().filter(Attendance::isPresent).count();
        return (double) attendedLessons / totalLessons * 100;
    }


    public double calculateQuizGradePercentage(Long studentId) {
        List<AutomatedFeedBack> allQuizzes = feedBackRepository.findByStudentId(studentId);
        double totalGrade = 0;
        double totalMaxGrade = 0;

        for (AutomatedFeedBack quizFeedback : allQuizzes) {
            totalGrade += quizFeedback.getGrade();
            totalMaxGrade += quizFeedback.getTotalNumberOfQuestions();
        }
        if (totalMaxGrade == 0) {
            return 0;
        }

        return (totalGrade / totalMaxGrade) * 100;
    }


    public double calculateAssignmentScorePercentage(Long studentId) {
        List<AssignmentSubmission> submissions = assignmentSubmissionRepository.findByStudentId(studentId);
        double totalScore = 0;
        double totalMaxScore = 0;

        for (AssignmentSubmission submission : submissions) {
            totalScore += submission.getGrade();
            totalMaxScore += submission.getTotal();
        }

        if (totalMaxScore == 0) {
            return 0;
        }

        return (totalScore / totalMaxScore) * 100;
    }

    //per student
    public StudentPerformance getPerformanceForStudent(Long studentId) {
        String username = studentRepository.findById(studentId).get().getUsername();
        double quizGrade = calculateQuizGradePercentage(studentId);
        double attendancePercentage = calculateAttendancePercentage(studentId);
        double assignmentScore = calculateAssignmentScorePercentage(studentId);
        return new StudentPerformance(username, studentId, quizGrade, attendancePercentage, assignmentScore);
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
