package org.example.lmsproject.userPart.service;

//import org.example.lmsproject.userPart.model.Student;
//import org.example.lmsproject.userPart.repository.StudentRepository;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    //make a virtual version of this database
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_get_all_students() {
        // Arrange
        Student student1 = new Student("nada", "123456789", "n@gmail.com");
        Student student2 = new Student("zinab", "123456789", "z@gmail.com");
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));
        // Act
        List<Student> result = studentService.getAllStudents();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void test_get_student_by_id_found() {
        // Arrange
        long id = 1L;
        Student student = new Student("mariam", "mm11", "m@gmail.com");
        student.setId(id);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        // Act
        Student result = studentService.getStudentById(id);
        // Assert
        assertNotNull(result);
        assertEquals("mariam", result.getUsername());
        assertEquals(id, result.getId());
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void test_get_student_by_id_notFound() {
        // Arrange
        long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());
        // Act
        Student result = studentService.getStudentById(id);
        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void test_find_student_by_username_found() {
        // Arrange
        String username = "menna";
        Student student = new Student(username, "123456789", "m@gmail.com");
        when(studentRepository.findAll()).thenReturn(List.of(student));
        // Act
        Student result = studentService.findStudentByUsername(username);
        // Assert
        assertNotNull(result);
        assertEquals("menna", result.getUsername());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void test_find_student_by_username_notFound() {
        // Arrange
        String username = "zinab";
        when(studentRepository.findAll()).thenReturn(List.of());
        // Act
        Student result = studentService.findStudentByUsername(username);
        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findAll();
    }
}