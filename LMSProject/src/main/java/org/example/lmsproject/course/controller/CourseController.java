package org.example.lmsproject.course.controller;

import java.security.Principal;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CourseController {

    private final CourseService courseService;
    private final InstructorService instructorService;

    @Autowired
    CourseController(CourseService courseService, InstructorService instructorService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
    }

    @GetMapping("/admin/courses")// for admin only
    public ResponseEntity<String> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/student/courses")
    public ResponseEntity<String> getAvailableCourses() {
        return ResponseEntity.ok(courseService.getAvailableCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<String> viewCourse(@PathVariable long courseId) {
        return ResponseEntity.ok(courseService.viewCourse(courseId));
    }

    @PostMapping("/instructor/courses")
    public ResponseEntity<String> createCourse(@RequestBody Course course, Principal principal) {
        if (course == null)
            return ResponseEntity.badRequest().body("Course cannot be null");
        if (courseService.courseExists(course.getTitle()))
            return ResponseEntity.badRequest().body("Course already exists");
        String instructorUsername = principal.getName();
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (instructor == null) {
            return ResponseEntity.badRequest().body("Instructor not found");
        }
        courseService.createCourse(course, instructor);  // Delegate to CourseService
        return ResponseEntity.ok("Course created successfully with ID of " + course.getCourseId());
    }

    @PutMapping("/instructor/courses/{courseId}") // instructor
    public ResponseEntity<String> updateCourse(@PathVariable long courseId, @RequestBody Course course) {
        if (courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        courseService.updateCourse(courseId, course);
        return ResponseEntity.ok(courseService.viewCourse(courseId));
    }

    @GetMapping("api/courses/{courseId}/students")
    public ResponseEntity<String> viewEnrolledStudents(@PathVariable long courseId) {
        if (courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        return ResponseEntity.ok(courseService.viewEnrolledStudents(courseId));
    }

    @PutMapping("/student/courses/{courseId}")
    public ResponseEntity<String> enrollInCourse(@PathVariable long courseId, Principal principal) {
        if (!courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        String studentUsername = principal.getName();
        return courseService.enrollStudentInCourse(courseId, studentUsername);
    }

    @DeleteMapping("/instructor/courses/{courseId}/students/{studentId}")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable long courseId, @PathVariable long studentId) {
        if (courseService.courseExists(courseId))
            return ResponseEntity.badRequest().body("Course not found");
        courseService.removeStudentFromCourse(courseId, studentId);
        return ResponseEntity.ok(courseService.viewEnrolledStudents(courseId));
    }

    @PutMapping("/instructor/courses/{courseId}/upload")
    public ResponseEntity<String> uploadMaterial(@PathVariable long courseId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body("File is empty");
        }
        if (courseService.courseExists(courseId)){
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return courseService.uploadMaterial(courseId, file);
    }

    @GetMapping("/student/courses/{courseId}/materials/{filename}")
    public ResponseEntity<byte[]> getMaterial(@PathVariable long courseId, @PathVariable String filename) {
        if (courseService.courseExists(courseId)) {
            return ResponseEntity.badRequest().body(null);
        }
        ResponseEntity<byte[]> fileResponse = courseService.getMaterial(filename);
        fileResponse.getStatusCode();
        return fileResponse;
    }

    @DeleteMapping("/instructor/courses/{id}/materials/{filename}")
    public ResponseEntity<String> removeMaterial(@PathVariable long courseId, @PathVariable String filename) {
        if (courseService.courseExists(courseId)) {
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return courseService.deleteMaterial(filename);
    }

    @GetMapping("/instructor/courses/{courseId}/enrollments")
    public ResponseEntity<String> getEnrollments(@PathVariable long courseId, Principal principal) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.badRequest().body("Course not found");
        }
        return ResponseEntity.ok(courseService.getEnrollments(course));
    }

    @PutMapping("/instructor/courses/{courseId}/enrollments/{requestId}/")
    public ResponseEntity<String> handleEnrollmentRequest(@PathVariable long courseId, @PathVariable long requestId,
                                                          @RequestParam boolean isAccepted) {
        if (!courseService.courseExists(courseId)) {
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return courseService.updateEnrollmentStatus(requestId, isAccepted);

    }

    @DeleteMapping("/instructor/courses/{courseId}") // instructor
    public void deleteCourse(@PathVariable long courseId) {
        courseService.deleteCourse(courseId);
    }
}