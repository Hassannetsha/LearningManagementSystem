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

    public Course save(Course course) {
        System.out.println("Test " + course.getTitle());
        return courseRepository.save(course);
    }

    public Course updateCourse(long id, Course course) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            if (course.getTitle()!=null) {
                existingCourse.setTitle(course.getTitle());
            }
            if (course.getDescription()!=null) {
                existingCourse.setDescription(course.getDescription());
            }
            if (course.getDuration()!=0) {
                existingCourse.setDuration(course.getDuration());
            }
            if (course.getStudents()!=null) {
                existingCourse.setStudents(course.getStudents());
            }
            if (course.getInstructor()!=null) {
                existingCourse.setInstructor(course.getInstructor());
            }
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }
}
