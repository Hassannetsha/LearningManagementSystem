package org.example.lmsproject.course.controller;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

//    @Autowired
    private final InstructorService instructorService;

    @Autowired
    CourseController(CourseService courseService, InstructorService instructorService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("/instructor/courses")
    public Course createCourse(@RequestBody Course course) {
//        return courseService.createCourse(course);
        Instructor instructor = instructorService.findById(course.getInstructor().getId());

        course.setInstructor(instructor);
        courseService.save(course);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Course created successfully");
        return course;
    }


    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable long id) {
        courseService.deleteCourse(id);
    }

}
