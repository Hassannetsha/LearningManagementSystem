package org.example.lmsproject.course.controller;

import java.security.Principal;
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

    @GetMapping("/{id}")
    public ResponseEntity<String> viewCourse(@PathVariable long id) {
        return ResponseEntity.ok(courseService.viewCourse(id));
    }

    @PostMapping("/instructor/courses/addCourse")
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

    @PutMapping("/instructor/courses/{id}") // instructor
    public ResponseEntity<String> updateCourse(@PathVariable long id, @RequestBody Course course) {
        if (courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        courseService.updateCourse(id, course);
        return ResponseEntity.ok(courseService.viewCourse(id));
    }

    @GetMapping("api/courses/{id}/students")
    public ResponseEntity<String> viewEnrolledStudents(@PathVariable long id) {
        if (courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        return ResponseEntity.ok(courseService.viewEnrolledStudents(id));
    }

    @PutMapping("/student/courses/{id}")
    public ResponseEntity<String> enrollInCourse(@PathVariable long id, Principal principal) {

        if (!courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        String studentUsername = principal.getName();
        return courseService.enrollStudentInCourse(id, studentUsername);
    }

    @GetMapping("/instructor/courses/{id}/removeStudent")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable long id, @RequestBody Student student) {
        if (courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        Long studentId = student.getId();
        courseService.removeStudentFromCourse(id, studentId);
        return ResponseEntity.ok(courseService.viewEnrolledStudents(id));
    }

    @PutMapping("/instructor/courses/{id}/upload")
    public ResponseEntity<String> uploadMaterial(@PathVariable long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body("File is empty");
        }
        if (courseService.courseExists(id)){
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return courseService.uploadMaterial(id, file);
    }

    @GetMapping("/student/courses/{id}/{filename}")
    public ResponseEntity<byte[]> getMaterial(@PathVariable long id, @PathVariable String filename) {
        if (courseService.courseExists(id)) {
            return ResponseEntity.badRequest().body(null);
        }
        ResponseEntity<byte[]> fileResponse = courseService.getMaterial(filename);
        fileResponse.getStatusCode();
        return fileResponse;
    }

    @DeleteMapping("/instructor/courses/{id}/removeMedia/{filename}")
    public ResponseEntity<String> removeMaterial(@PathVariable long id, @PathVariable String filename) {
        if (courseService.courseExists(id)) {
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return courseService.deleteMaterial(filename);
    }

    @GetMapping("/instructor/courses/{id}/getEnrollments")
    public ResponseEntity<String> getEnrollments(@PathVariable long id, Principal principal) {
        Course course = courseService.getCourseById(id);
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

    @DeleteMapping("/instructor/courses/{id}") // instructor
    public void deleteCourse(@PathVariable long id) {
        courseService.deleteCourse(id);
    }
}