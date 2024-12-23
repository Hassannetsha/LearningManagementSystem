package org.example.lmsproject.Assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsproject.assignment.controller.AssignmentController;
import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.assignment.model.FeedbackResponse;
import org.example.lmsproject.assignment.service.AssignmentService;
import org.example.lmsproject.course.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AssignmentControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AssignmentService assignmentservice;

    @InjectMocks
    private AssignmentController assignmentcontroller;

    private ObjectMapper objectmapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assignmentcontroller).build();
        objectmapper = new ObjectMapper();
    }

    @Test
    void test_createassignment() throws Exception {
        Long courseid = 1L;
        String title = "Test Assignment";
        String description = "Test Description";
        LocalDate deadline = LocalDate.of(2024, 12, 31);

        Assignment createdassignment = new Assignment();
        createdassignment.setTitle(title);
        createdassignment.setDescription(description);
        createdassignment.setDeadline(deadline);
        when(assignmentservice.createAssignment(eq(courseid), eq(title), eq(description), eq(deadline)))
                .thenReturn(createdassignment);

        mockMvc.perform(post("/instructor/assignments/create")
                        .param("courseid", String.valueOf(courseid))
                        .param("title", title)
                        .param("description", description)
                        .param("deadline", deadline.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.deadline").value(deadline.toString()));  // Expect the date "2024-12-31"

        verify(assignmentservice, times(1)).createAssignment(eq(courseid), eq(title), eq(description), eq(deadline));
    }


    @Test
    void test_getassignmentbycourseid() throws Exception {
        Long courseId = 1L;
        List<Assignment> assignments = List.of(new Assignment());

        when(assignmentservice.getassignmentsbycourseid(eq(courseId))).thenReturn(assignments);

        mockMvc.perform(get("/api/getassignmentbycourse")
                        .param("courseid", String.valueOf(courseId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(assignments.size()));

        verify(assignmentservice, times(1)).getassignmentsbycourseid(eq(courseId));
    }



    @Test
    void test_gradesubmission() throws Exception {
        Long submissionid = 1L;
        Integer grade = 85;
        Integer total = 85;
        String feedback = "you can do better";

      
        Assignment assignment = new Assignment();
        assignment.setId(1L);  
        AssignmentSubmission gradedsubmission = new AssignmentSubmission();
        gradedsubmission.setGrade(grade);
        gradedsubmission.setFeedback(feedback);
        gradedsubmission.setAssignment(assignment); 
        gradedsubmission.setSubmissiontime(LocalDateTime.now());  

        String expectedResponse = gradedsubmission.toString();

        when(assignmentservice.gradesubmission(eq(submissionid), eq(grade), eq(total), eq(feedback)))
                .thenReturn(gradedsubmission);

        mockMvc.perform(post("/instructor/gradesubmission")
                        .param("submissionid", String.valueOf(submissionid))
                        .param("grade", String.valueOf(grade))
                        .param("feedback", feedback)
                        .param("total", String.valueOf(total)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(assignmentservice, times(1)).gradesubmission(eq(submissionid), eq(grade), eq(total), eq(feedback));
    }





    @Test
    void test_updateassignment() throws Exception {
        Long assignmentid = 1L;
        String title = "new title";
        String description = "new description";
        LocalDate deadline = LocalDate.of(2025, 1, 1);

        Course course = new Course();
        course.setCourseId(1L);  

        Assignment updatedassignment = new Assignment();
        updatedassignment.setTitle(title);
        updatedassignment.setDescription(description);
        updatedassignment.setDeadline(deadline);
        updatedassignment.setCourse(course); 

        when(assignmentservice.updateassignment(eq(assignmentid), eq(title), eq(description), eq(deadline)))
                .thenReturn(updatedassignment);

        mockMvc.perform(put("/instructor/update")
                        .param("assignmentId", String.valueOf(assignmentid))
                        .param("title", title)
                        .param("description", description)
                        .param("deadline", deadline.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(assignmentservice, times(1)).updateassignment(eq(assignmentid), eq(title), eq(description), eq(deadline));
    }


    @Test
    void test_deleteassignment() throws Exception {
        Long assignmentId = 1L;

        doNothing().when(assignmentservice).deleteassignment(eq(assignmentId));

        mockMvc.perform(delete("/instructor/deleteassignment")
                        .param("assignmentId", String.valueOf(assignmentId)))
                .andExpect(status().isNoContent());

        verify(assignmentservice, times(1)).deleteassignment(eq(assignmentId));
    }

    @Test
    void test_getfeedback() throws Exception {
        Long submissionid = 1L;
        FeedbackResponse feedbackresponse = new FeedbackResponse("you can do better", 85);
        when(assignmentservice.getsubmissionfeedbackandgrade(eq(submissionid))).thenReturn(feedbackresponse);
        mockMvc.perform(get("/student/feedback_grade")
                        .param("submissionId", String.valueOf(submissionid)))
                .andExpect(status().isOk());

        verify(assignmentservice, times(1)).getsubmissionfeedbackandgrade(eq(submissionid));
    }
}
