package org.example.lmsproject.userPart.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student findStudentByUsername(String username) {
        return  studentRepository.findAll().stream().filter(student -> student.getUsername().equals(username)).findFirst().orElse(null);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public void saveAll(List<Student> students) {
        studentRepository.saveAll(students);
    }

    public String getEnrolledCourses(String studentUsername) {
        Student student = findStudentByUsername(studentUsername);
        List<Course> courses = student.getCourses();
        return courses.stream()
                .map(Course::toString)
                .collect(Collectors.joining(",\n\n    ", "[\n    ", "]\n"));
    }
}
