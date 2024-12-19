package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAll().stream().filter(Course::isAvailable).collect(Collectors.toList());
    }

    public Course getCourseById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void createCourse(Course course, Instructor instructor) {
        course.setInstructor(instructor);  // Assign the instructor to the course
        courseRepository.save(course);  // Save the course to the database
    }

    public Course updateCourse(long id, Course course) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            if (course.getTitle() != null) {
                existingCourse.setTitle(course.getTitle());
            }
            if (course.getDescription() != null) {
                existingCourse.setDescription(course.getDescription());
            }
            if (course.getDuration() != 0) {
                existingCourse.setDuration(course.getDuration());
            }
            if (course.isAvailable() != null) {
                existingCourse.setAvailable(course.isAvailable());
            }
            if (course.getStudents() != null) {
                existingCourse.setStudents(course.getStudents());
            }
            if (course.getInstructor() != null) {
                existingCourse.setInstructor(course.getInstructor());
            }
            if (course.getAssignments() != null) {
                existingCourse.setAssignments(course.getAssignments());
            }
            if (course.getLessons() != null) {
                existingCourse.setLessons(course.getLessons());
            }
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public List<Student> viewEnrolledStudents(long id) {
        Course course = getCourseById(id);
        return course != null ? course.getStudents() : List.of();
    }

    public void enrollStudentInCourse(long courseId, String studentUsername) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Student student = studentService.findStudentByUsername(studentUsername);
        if (student != null) {
            course.addStudent(student);
            courseRepository.save(course);
        } else {
            throw new RuntimeException("Student not found");
        }
    }

    public boolean removeStudentFromCourse(long courseId, long studentId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        Student student = studentService.getStudentById(studentId);
        if (course != null && student != null) {
            course.removeStudent(student);
            student.getCourse().remove(course);
            courseRepository.save(course);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    // Delete a course by ID
    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }
}
