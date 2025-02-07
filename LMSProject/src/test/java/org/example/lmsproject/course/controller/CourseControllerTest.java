package org.example.lmsproject.course.controller;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.AbstractMockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    @WithMockUser(username = "instructor1")
    void testCreateCourse() throws Exception {
        long courseId = 1L;
        Principal principal = () -> "instructor1";
        Course course = new Course("Math", "A basic math course", 30, true);
        course.setCourseId(courseId);

        mockMvc.perform(post("/instructor/courses")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andExpect(content().string("Course created successfully with ID of 1"))
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody);
                    assertTrue(responseBody.contains("Course created successfully with ID of 1"));
                });
    }

//    @Test
//    @WithMockUser(username = "student1")
//    void testEnrollStudentInCourse() throws Exception {
//        // Arrange
//        long courseId = 1L;
//        Course course = new Course("Math", "A basic math course", 30, true);
//
//        // Mock repository to return course when found
//        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
//        when(courseRepository.existsById(courseId)).thenReturn(true);
//
//        // Mock service layer for course existence check and enrollment behavior
//        when(courseService.courseExists(courseId)).thenReturn(true);
//        when(courseService.enrollStudentInCourse(courseId, "student1")).thenReturn("A new enrollment request has been sent to the instructor");
//
//        // Act & Assert
//        mockMvc.perform(put("/student/courses/{courseId}", courseId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("A new enrollment request has been sent to the instructor"))
//                .andDo(MockMvcResultHandlers.print());
//    }


}
