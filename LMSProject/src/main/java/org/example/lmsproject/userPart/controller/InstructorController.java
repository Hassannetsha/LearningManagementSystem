//package org.example.lmsproject.userPart.controller;
//
//import org.example.lmsproject.course.model.Course;
//import org.example.lmsproject.course.service.CourseService;
//import org.example.lmsproject.userPart.model.Instructor;
//import org.example.lmsproject.userPart.service.InstructorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("/instructor")
//public class InstructorController {
//
//    private final InstructorService instructorService;
//    private final CourseService courseService;
//
//    @Autowired
//    public InstructorController(InstructorService instructorService, CourseService courseService) {
//        this.instructorService = instructorService;
//        this.courseService = courseService;
//    }
//
//    @PostMapping("/addCourse")
//    public ResponseEntity<String> addCourse(@RequestBody Course course, Principal principal) {
//        String instructorUsername = principal.getName();
//        Instructor instructor = instructorService.findByUsername(instructorUsername);
//        if (instructor == null) {
//            return ResponseEntity.badRequest().body("Instructor not found");
//        }
//        course.setInstructor(instructor);
//        courseService.save(course);
//        return ResponseEntity.ok("Course added successfully " + course.getTitle());
//    }
//
//    @DeleteMapping("/deleteCourse/{courseId}")
//    public ResponseEntity<String> deleteCourse(@PathVariable long courseId) {
//        Course course = courseService.getCourseById(courseId);
//        if (course == null) {
//            return ResponseEntity.badRequest().body("Course not found");
//        }
//        courseService.deleteCourse(courseId);
//        return ResponseEntity.ok("Course deleted successfully " + course.getTitle());
//    }
//
//    @PutMapping("/updateCourse/{courseId}")
//    public ResponseEntity<String> updateCourse(@PathVariable long courseId, @RequestBody Course updatedCourse) {
//        System.out.println("UPDATE");
//        Course course = courseService.getCourseById(courseId);
//
//        if (course == null) {
//            return ResponseEntity.badRequest().body("Course not found");
//        }
//        courseService.updateCourse(courseId, updatedCourse);
//        return ResponseEntity.ok("Course updated successfully " + course.getTitle());
//    }
//
//}
