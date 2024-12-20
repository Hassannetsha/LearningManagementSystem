package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;


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

    public String uploadMaterial(long id, MultipartFile file){
        Course course = courseRepository.findById(id).orElse(null);
        try {
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                System.out.println("Directory does not exist");
                directory.mkdirs();
            }
            byte[] bytes = file.getBytes();
            System.out.println(uploadDirectory+file.getOriginalFilename());
            Path path = Paths.get(uploadDirectory + file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("File written");
        }catch (IOException e){}
        return "File uploaded successfully";
    }

    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }
}
