package org.example.lmsproject.course.controller;


import org.example.lmsproject.course.model.Attendance;
import org.example.lmsproject.course.model.Lesson;
import org.example.lmsproject.course.repository.AttendanceRepository;
import org.example.lmsproject.course.repository.LessonRepository;
import org.example.lmsproject.course.service.LessonService;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestParam Long lessonId) {
        String otp = lessonService.generateOtp(lessonId);
        return ResponseEntity.ok("OTP generated: " + otp);
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollInLesson(@RequestParam Long lessonId, @RequestParam String otp, @RequestParam Long studentId) {
        System.out.println("Trying to enroll student: " + studentId + " in Lesson: " + lessonId + " with OTP: " + otp);
        boolean isValid = lessonService.validateOtp(lessonId, otp);

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Attendance existingAttendance = attendanceRepo.findByLessonAndStudent(lesson, student);
        if (existingAttendance != null) {
            return ResponseEntity.badRequest().body("Student already enrolled in this lesson.");
        }
        Attendance attendance = new Attendance();
        attendance.setLesson(lesson);
        attendance.setStudent(student);
        attendance.setEnrolled(true);
        attendance.setPresent(true);

        attendanceRepo.save(attendance);

        return ResponseEntity.ok("Student successfully enrolled in the lesson.");
    }

    // Endpoint for validating OTP and marking attendance
    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam Long lessonId, @RequestParam String otp, @RequestParam Long studentId) {
        boolean isValid = lessonService.validateOtp(lessonId, otp);

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Attendance attendance = new Attendance(); //mark attendance
        attendance.setLesson(lesson); 
        attendance.setStudent(student);
        attendance.setPresent(true);

        // Save attendance
        attendanceRepo.save(attendance);

        return ResponseEntity.ok("Attendance marked successfully!");
    }
}
