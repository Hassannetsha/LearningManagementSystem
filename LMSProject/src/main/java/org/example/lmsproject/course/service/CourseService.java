package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.repository.CourseMaterialRepository;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {



    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseMaterialService courseMaterialService;
    @Autowired
    private CourseMaterialRepository courseMaterialRepository;


    public boolean courseExists(long courseId) {
        return courseRepository.existsById(courseId);
    }

    public String getAllCourses() {
         List<Course> courses = courseRepository.findAll();
        String jsonResponse = courses.stream()
                .map(Course::toString)
                .collect(Collectors.joining(",\n\n    ", "[\n    ", "]\n")); // Join all course strings into a JSON array format

        return jsonResponse;
    }

    public String getAvailableCourses() {
        List<Course> availableCourses = courseRepository.findAll().stream().filter(Course::isAvailable).collect(Collectors.toList());
        String jsonResponse = availableCourses.stream()
                .map(Course::toString)
                .collect(Collectors.joining(",\n\n    ", "[\n    ", "]\n"));
        return jsonResponse;
    }

    public String viewCourse(long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return "";
        }
        return course.toString();
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

    public String viewEnrolledStudents(long id) {
        Course course = getCourseById(id);
        List<Long> studentIds = course.getStudents().stream()
                .map(Student::getId)
                .toList();

        // Convert the list of IDs to a JSON-style string
        return "{\"students\": " + studentIds.toString() + "}";
    }

    public boolean enrollStudentInCourse(long courseId, String studentUsername) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Student student = studentService.findStudentByUsername(studentUsername);

        if (student != null) {
            System.out.println("Student: " + student.getId());
            course.addStudent(student);
            student.getCourses().add(course);
            courseRepository.save(course);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    public boolean removeStudentFromCourse(long courseId, long studentId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        Student student = studentService.getStudentById(studentId);
        if (course != null && student != null) {
            course.removeStudent(student);
            student.getCourses().remove(course);
            courseRepository.save(course);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    public ResponseEntity<String> uploadMaterial(long id, MultipartFile file) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return ResponseEntity.status(404).body("Course not found");
        }

        ResponseEntity<String> uploadResponse = courseMaterialService.uploadMaterial(course, file);
        if (uploadResponse.getStatusCode() != HttpStatus.OK) {
            return uploadResponse;
        }

        CourseMaterial courseMaterial = courseMaterialService.getByFilename(file.getOriginalFilename());
        if (courseMaterial != null) {
            course.addMaterial(courseMaterial);
            courseRepository.save(course);
            return ResponseEntity.ok("File uploaded and associated with the course successfully");
        }

        return ResponseEntity.ok("File upload failed to associate with the course");
    }


    public ResponseEntity<byte[]> getMaterial(String filename) {
        return courseMaterialService.getMaterial(filename);
    }

    public ResponseEntity<String> deleteMaterial(long id, String filename) {
        return courseMaterialService.deleteMaterial(filename);
    }

    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }
}
