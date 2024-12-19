package org.example.lmsproject.course.controller;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/student/courses")
    public List<Course> getAvailableCourses() {
        return courseService.getAvailableCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable long id) {
        return courseService.getCourseById(id);
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
    public Course updateCourse(@PathVariable long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    @GetMapping("api/courses/{id}/students")
    public List<Student> viewEnrolledStudents(@PathVariable long id) {
        return courseService.viewEnrolledStudents(id);
    }

    @PutMapping("/student/courses/{id}")
    public ResponseEntity<String> enrollInCourse(@PathVariable long id, Principal principal) {
        String studentUsername = principal.getName();
        courseService.enrollStudentInCourse(id, studentUsername);  // Delegate to CourseService
        return ResponseEntity.ok(studentUsername + " enrolled successfully");
    }

    @GetMapping("/instructor/courses/{id}/removeStudent")
    public List<Student> removeStudentFromCourse(@PathVariable long id, @RequestBody Student student) {
        Long studentId = student.getId();
        courseService.removeStudentFromCourse(id, studentId);
        return courseService.getCourseById(id).getStudents();
    }

    @DeleteMapping("/instructor/courses/{id}") // instructor
    public void deleteCourse(@PathVariable long id) {
        courseService.deleteCourse(id);
    }
}
