package org.example.lmsproject.quiz;


import java.util.Arrays;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.quiz.Controllers.Quiz.QuizController;
import org.example.lmsproject.quiz.DTOs.Quizzes.QuizCreationDTO;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizRepository;
import org.example.lmsproject.quiz.Services.Question.QuestionServices;
import org.example.lmsproject.quiz.Services.Quizzes.QuizServices;
import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.User;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;


public class QuizControllerTest {

    private MockMvc mockMvc;
    @Mock

    private QuizServices quizServices;
    @Mock
    private QuestionServices questionServices;
    @Mock
    private CourseService courseService;
    @Mock
    private QuizRepository quizRepo;

    @InjectMocks
    private QuizController quizController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddNewQuiz() throws Exception {
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        instructor.setUsername("menna");
        instructor.setEmail("menna@example.com");
        instructor.setRole(User.Role.ROLE_INSTRUCTOR);
        instructor.setPassword("password123");
        Course course = new Course();
        course.setCourseId(1L);
        course.setDescription("nn");
        course.setAvailable(true);
        course.setDuration(7);
        course.setInstructor(instructor);
        QuestionEntity question = new MCQQuestionEntity(1L, "What is the capital of Egypt?", "mcq", course,
                Arrays.asList("Cairo", "giza", "Alexandria", "Luxor"), "Cairo");
        QuestionBank questionBank = new QuestionBank(1L, "geography Bank", course, Arrays.asList(question));
        QuizEntity quiz = new QuizEntity(1L, course, "Geography Quiz", questionBank);
        when(courseService.getCourseById(1L)).thenReturn(course);
        when(questionServices.findQuestionBankByid(1L)).thenReturn(questionBank);
        Mockito.when(quizServices.addNewQuiz(Mockito.any(QuizCreationDTO.class))).thenReturn(quiz);
        mockMvc.perform(post("/instructor/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quiz)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.quizName").value("Geography Quiz"));
    }

    @Test
    void testUpdateQuiz() throws Exception {
        Long quizId = 1L;
        Long courseId = 2L;
        Long questionBankId = 3L;
        String updatedQuizName = "Updated Quiz";

        QuizEntity quiz = new QuizEntity(quizId, null, "Original Quiz", null);

        when(quizServices.findById(quizId)).thenReturn(quiz);
        doNothing().when(quizServices).updateQuiz(quizId, courseId, updatedQuizName, questionBankId);

        mockMvc.perform(put("/instructor/quizzes/" + quizId)
                .param("courseId", String.valueOf(courseId))
                .param("quizName", updatedQuizName)
                .param("questionBankId", String.valueOf(questionBankId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testDeleteQuiz() throws Exception {
        Long quizId = 1L;

        QuizEntity quiz = new QuizEntity(quizId, null, "Test Quiz", null);

        when(quizRepo.findByquizId(quizId)).thenReturn(quiz);
        doNothing().when(quizServices).deleteQuiz(quizId);

        mockMvc.perform(delete("/instructor/quizzes/" + quizId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
