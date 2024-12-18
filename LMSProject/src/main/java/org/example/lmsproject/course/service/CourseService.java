package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        System.out.println("Test " + course.getTitle());
        return courseRepository.save(course);
    }

    public Course updateCourse(long id, Course course) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            existingCourse.setTitle(course.getTitle());
            existingCourse.setDescription(course.getDescription());
            existingCourse.setDuration(course.getDuration());
            existingCourse.setStudents(course.getStudents());
            existingCourse.setInstructor(course.getInstructor());
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }
}
