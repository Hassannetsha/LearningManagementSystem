package org.example.lmsproject.quiz.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Arrays;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.Controllers.Question.QuestionController;
import org.example.lmsproject.quiz.DTOs.Questions.QuestionBankDTO;
import org.example.lmsproject.quiz.Repositories.Question.QuestionBankRepository;
import org.example.lmsproject.quiz.Services.Question.QuestionServices;
import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionControllerTest {

    private MockMvc mockMvc;
    @Mock
    private QuestionServices questionServices;
    @Mock
    private QuestionBankRepository questionBankRepository;

    @InjectMocks
    private QuestionController questionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddNewQuestionBank() throws Exception {
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        instructor.setUsername("menna");
        instructor.setEmail("menna@example.com");
        instructor.setRole(User.Role.ROLE_INSTRUCTOR);
        instructor.setPassword("password123");
        Course course = new Course("science", "nn", 7, true);
        course.setInstructor(instructor);
        course.setCourseId(1L);
        QuestionEntity question = new MCQQuestionEntity(1L, "What is the capital of Egypt?", "mcq", course,
                Arrays.asList("Cairo", "giza", "Alexandria", "Luxor"), "Cairo");
        QuestionBank questionBank = new QuestionBank(1L, "geography Bank", course, Arrays.asList(question));
        Mockito.doNothing().when(questionServices).addNewQuestionBank(Mockito.any(QuestionBankDTO.class));

        mockMvc.perform(post("/instructor/questions/Banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionBank)))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeleteQeustionBank()throws Exception{
        Long questionBankId = 1L;

        QuestionBank questionBank = new QuestionBank(questionBankId, "geography Bank", null, null);

        when(questionBankRepository.findByid(questionBankId)).thenReturn(questionBank);
        doNothing().when(questionServices).deleteQuestionBank(questionBankId);

        mockMvc.perform(delete("/instructor/questions/Banks/" + questionBankId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testUpdateQuiz() throws Exception {
        Long questionBankId = 1L;
        Long courseId = 2L;
        String updatedQuestionBankName = "math Quiz";

        QuestionBank questionBank = new QuestionBank(questionBankId, "geography Bank", null, null);

        when(questionBankRepository.findByid(questionBankId)).thenReturn(questionBank);
        doNothing().when(questionServices).updateQuestionBank(questionBankId, courseId, updatedQuestionBankName, null);

        mockMvc.perform(put("/instructor/questions/Banks/" + questionBankId)
                .param("courseId", String.valueOf(courseId))
                .param("questionBankName", updatedQuestionBankName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
