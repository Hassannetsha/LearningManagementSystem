package org.example.lmsproject.Lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.lmsproject.course.controller.LessonController;
import org.example.lmsproject.course.model.Lesson;
import org.example.lmsproject.course.repository.AttendanceRepository;
import org.example.lmsproject.course.repository.LessonRepository;
import org.example.lmsproject.course.service.LessonService;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttendanceRepository attendanceRepo;

    @InjectMocks
    private LessonController lessonController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
    }

    @Test
    void testGenerateOtp() throws Exception {
        Long lessonId = 1L;
        String otp = "123456";

        when(lessonService.generateOtp(lessonId)).thenReturn(otp);

        mockMvc.perform(post("/instructor/generate-otp")
                        .param("lessonId", String.valueOf(lessonId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP generated: " + otp));
    }

    @Test
    void testEnrollInLesson() throws Exception {
        Long lessonId = 1L;
        Long studentId = 1L;
        String otp = "123456";

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        User student = new User();
        student.setId(studentId);
        student.setUsername("john_doe");

        when(lessonService.validateOtp(lessonId, otp)).thenReturn(true);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));

        mockMvc.perform(post("/student/enroll")
                        .param("lessonId", String.valueOf(lessonId))
                        .param("otp", otp)
                        .param("studentId", String.valueOf(studentId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Student successfully enrolled in the lesson."));
    }

    @Test
    void testEnrollInLessonInvalidOtp() throws Exception {
        Long lessonId = 1L;
        Long studentId = 1L;
        String otp = "123456";

        when(lessonService.validateOtp(lessonId, otp)).thenReturn(false);

        mockMvc.perform(post("/student/enroll")
                        .param("lessonId", String.valueOf(lessonId))
                        .param("otp", otp)
                        .param("studentId", String.valueOf(studentId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or expired OTP."));
    }

    @Test
    void testCreateLesson() throws Exception {
        Long courseId = 1L;
        Lesson lesson = new Lesson();
        lesson.setTitle("testl");
        lesson.setDescription("testd");

        lesson.setDateTime(LocalDateTime.of(2023, 12, 23, 14, 30));

        when(lessonService.createLesson(eq(courseId), any(Lesson.class))).thenReturn(lesson);

        mockMvc.perform(post("/instructor/createlesson")
                        .param("courseId", String.valueOf(courseId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                });
    }
    

    @Test
    void testDeleteLesson() throws Exception {
        Long lessonId = 1L;

        mockMvc.perform(delete("/instructor/deletelesson/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(content().string("Lesson deleted successfully"));
    }
}
