package org.example.lmsproject.course.controller;

import java.security.Principal;
import java.util.Optional;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CourseController {

    private final CourseService courseService;

    @Autowired
    CourseController(CourseService courseService, InstructorService instructorService) {
        this.courseService = courseService;
    }

    @GetMapping("/admin/courses")// for admin only
    public ResponseEntity<String> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/student/courses/available")
    public ResponseEntity<String> getAvailableCourses() {
        return ResponseEntity.ok(courseService.getAvailableCourses());
    }

    @GetMapping("/student/courses/enrolled")
    public ResponseEntity<String> getEnrolledCourses(Principal principal) {
        return ResponseEntity.ok(courseService.getEnrolledCourses(principal.getName()));
    }

    @GetMapping("/api/{courseId}")
    public ResponseEntity<String> viewCourse(@PathVariable long courseId) {
        if (!courseService.courseExists(courseId)) {
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return ResponseEntity.ok(courseService.viewCourse(courseId));
    }

    @GetMapping("/student/courses/{courseId}")
    public ResponseEntity<String> viewAvailableCourse(@PathVariable long courseId) {
        return ResponseEntity.ok(courseService.viewAvailableCourse(courseId));
    }

    @PostMapping("/instructor/courses")
    public ResponseEntity<String> createCourse(@RequestBody Course course, Principal principal) {
        if (course == null)
            return ResponseEntity.badRequest().body("Course cannot be null");
        if (courseService.courseExists(course.getTitle()))
            return ResponseEntity.badRequest().body("Course already exists");
        String instructorUsername = principal.getName();
        courseService.createCourse(course, instructorUsername);
        return ResponseEntity.ok("Course created successfully with ID of " + course.getCourseId());
    }

    @PutMapping("/instructor/courses/{courseId}") // instructor
    public ResponseEntity<String> updateCourse(@PathVariable long courseId, @RequestBody Course course, Principal principal) {
        if (!courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        String instructorUsername = principal.getName();
        return ResponseEntity.ok(courseService.updateCourse(courseId, course, instructorUsername));
    }

    @GetMapping("api/courses/{courseId}/students")
    public ResponseEntity<String> viewEnrolledStudents(@PathVariable long courseId) {
        if (!courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        return ResponseEntity.ok(courseService.viewEnrolledStudents(courseId));
    }

    @PutMapping("/student/courses/{courseId}")
    public ResponseEntity<String> enrollInCourse(@PathVariable long courseId, Principal principal) {
        if (!courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not founddddd");
        String studentUsername = principal.getName();
        return ResponseEntity.ok(courseService.enrollStudentInCourse(courseId, studentUsername));
    }

    @DeleteMapping("/instructor/courses/{courseId}/students/{studentId}")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable long courseId, Principal principal,
                                                          @PathVariable long studentId) {
        if (!courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        String instructorUsername = principal.getName();
        courseService.removeStudentFromCourse(courseId, instructorUsername, studentId);
        return ResponseEntity.ok(courseService.viewEnrolledStudents(courseId));
    }

    @PutMapping("/instructor/courses/{courseId}/upload")
    public ResponseEntity<String> uploadMaterial(@PathVariable long courseId, Principal principal,
                                                 @RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.badRequest().body("File is empty");
        if (!courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course does not exist");
        String instructorUsername = principal.getName();
        return ResponseEntity.ok(courseService.uploadMaterial(courseId, instructorUsername, file));
    }

    @GetMapping("/student/courses/{courseId}/materials/{filename}")
    public ResponseEntity<byte[]> getMaterial(
            @PathVariable long courseId,
            Principal principal,
            @PathVariable String filename
    ) {
        String studentUsername = principal.getName();

        Optional<byte[]> material = courseService.getMaterial(courseId, studentUsername, filename);

        return material.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));

    }


    @DeleteMapping("/instructor/courses/{courseId}/materials/{filename}")
    public ResponseEntity<String> removeMaterial(@PathVariable long courseId, Principal principal, @PathVariable String filename) {
        if (!courseService.courseExists(courseId)) {
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        String instructorUsername = principal.getName();
        return ResponseEntity.ok(courseService.deleteMaterial(instructorUsername, courseId, filename));
    }

    @GetMapping("/instructor/courses/{courseId}/enrollments")
    public ResponseEntity<String> getEnrollments(@PathVariable long courseId, Principal principal) {
        Course course = courseService.getCourseById(courseId);
        if (course == null)
            return ResponseEntity.badRequest().body("Course not found");
        String instructorUsername = principal.getName();
        return ResponseEntity.ok(courseService.getEnrollments(instructorUsername, course));
    }

    @PutMapping("/instructor/courses/{courseId}/enrollments/{requestId}/")
    public ResponseEntity<String> handleEnrollmentRequest(@PathVariable long courseId, @PathVariable long requestId,
                                                          @RequestParam boolean isAccepted,
                                                          Principal principal) {
        if (!courseService.courseExists(courseId)) {
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        String instructorUsername = principal.getName();
        return ResponseEntity.ok(courseService.updateEnrollmentStatus(instructorUsername, requestId, isAccepted));
    }

    @DeleteMapping("/instructor/courses/{courseId}") // instructor
    public ResponseEntity<String> deleteCourse(Principal principal, @PathVariable long courseId) {
        String instructorUsername = principal.getName();
        return ResponseEntity.ok(courseService.deleteCourse(instructorUsername, courseId));
    }
}