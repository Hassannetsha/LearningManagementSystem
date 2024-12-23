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
import org.springframework.web.bind.annotation.*;

@RestController
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @PostMapping("/student/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestParam Long lessonId) {
        String otp = lessonService.generateOtp(lessonId);
        return ResponseEntity.ok("OTP generated: " + otp);
    }

    @PostMapping("/student/enroll")
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

    @PostMapping("/instructor/createlesson")
    public ResponseEntity<String> createLesson(
            @RequestParam Long courseId,
            @RequestBody Lesson lesson) {
        try {
            Lesson createdLesson = lessonService.createLesson(courseId, lesson);
            return ResponseEntity.ok(createdLesson.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/instructor/updatelesson/{id}")
    public ResponseEntity<String> updateLesson(
            @PathVariable Long id,
            @RequestBody Lesson updatedLesson) {
        try {
            Lesson lesson = lessonService.updateLesson(id, updatedLesson);
            return ResponseEntity.ok(lesson.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/instructor/deletelesson/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.ok("Lesson deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to delete lesson: " + e.getMessage());
        }
    }

}
