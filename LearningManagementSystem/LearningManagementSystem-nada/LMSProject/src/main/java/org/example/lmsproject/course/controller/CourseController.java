package org.example.lmsproject.course.controller;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import java.util.List;

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
        if (!courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        courseService.updateCourse(id, course);
        return ResponseEntity.ok(courseService.viewCourse(id));
    }

    @GetMapping("api/courses/{id}/students")
    public ResponseEntity<String> viewEnrolledStudents(@PathVariable long id) {
        if (!courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        return ResponseEntity.ok(courseService.viewEnrolledStudents(id));
    }

    @PutMapping("/student/courses/{id}")
    public ResponseEntity<String> enrollInCourse(@PathVariable long id, Principal principal) {
        if (!courseService.courseExists(id))
            return ResponseEntity.badRequest().body("Course not found");
        String studentUsername = principal.getName();
        System.out.println(studentUsername + " " + id);
        if(courseService.enrollStudentInCourse(id, studentUsername)) // Delegate to CourseService
            return ResponseEntity.ok(studentUsername + " enrolled successfully");
        return ResponseEntity.badRequest().body(studentUsername + " couldn't enroll");
    }

    @GetMapping("/instructor/courses/{id}/removeStudent")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable long id, @RequestBody Student student) {
        if (!courseService.courseExists(id))
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
        if (!courseService.courseExists(id)){
            return ResponseEntity.badRequest().body("Course does not exist");
        }
        return ResponseEntity.ok(courseService.uploadMaterial(id, file));
    }

    @DeleteMapping("/instructor/courses/{id}") // instructor
    public void deleteCourse(@PathVariable long id) {
        courseService.deleteCourse(id);
    }
}
