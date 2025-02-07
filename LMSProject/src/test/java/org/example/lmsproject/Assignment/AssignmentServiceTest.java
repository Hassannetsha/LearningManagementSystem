package org.example.lmsproject.Assignment;


import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.assignment.model.FeedbackResponse;
import org.example.lmsproject.assignment.repository.AssignmentRepository;
import org.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import org.example.lmsproject.assignment.service.AssignmentService;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentrepo;

    @Mock
    private AssignmentSubmissionRepository assignmentsubmissionrepo;

    @Mock
    private CourseRepository courserepo;

    @InjectMocks
    private AssignmentService assignmentservice;

    @Mock
    private MailboxService mailboxService;

    @Test
    public void test_createassignment() {
        Long courseId = 1L;
        String title = "Test Assignment";
        String description = "Test Description";
        LocalDate deadline = LocalDate.of(2024, 12, 31);

        Student student1 = new Student("Student1", "password1", "student1@example.com");
        Student student2 = new Student("Student2", "password2", "student2@example.com");

        Course mockCourse = new Course();
        mockCourse.setStudents(List.of(student1, student2));

        Assignment mockAssignment = new Assignment();
        mockAssignment.setTitle(title);
        mockAssignment.setDescription(description);
        mockAssignment.setDeadline(deadline);

        when(courserepo.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(assignmentrepo.save(any(Assignment.class))).thenReturn(mockAssignment);

        Assignment createdAssignment = assignmentservice.createAssignment(courseId, title, description, deadline);

        assertNotNull(createdAssignment);
        assertEquals(title, createdAssignment.getTitle());
        assertEquals(description, createdAssignment.getDescription());
        assertEquals(deadline, createdAssignment.getDeadline());

        verify(courserepo, times(1)).findById(courseId);
        verify(assignmentrepo, times(1)).save(any(Assignment.class));
        // verify(mailboxService, times(1)).addBulkNotifications(anyList(), any(Assignment.class));
    }



    @Test
    public void test_gradesubmission() {
        Long submissionId = 1L;
        Integer grade = 80;
        Integer total = 80;
        String feedback = "You can do better";

        AssignmentSubmission mockSubmission = new AssignmentSubmission();
        User mockStudent = new User();
        mockStudent.setId(1L);
        mockSubmission.setStudent(mockStudent);

        when(assignmentsubmissionrepo.findById(submissionId)).thenReturn(Optional.of(mockSubmission));
        when(assignmentsubmissionrepo.save(any(AssignmentSubmission.class))).thenReturn(mockSubmission);

        AssignmentSubmission gradedSubmission = assignmentservice.gradesubmission(submissionId, grade , total, feedback);

        assertNotNull(gradedSubmission);
        assertEquals(grade, gradedSubmission.getGrade());
        assertEquals(feedback, gradedSubmission.getFeedback());

        verify(assignmentsubmissionrepo, times(1)).findById(submissionId);
        verify(assignmentsubmissionrepo, times(1)).save(any(AssignmentSubmission.class));
        // verify(mailboxService, times(1)).addNotification(mockStudent.getId(), mockSubmission);
    }


    @Test
    public void test_updateassignment() {
        Long assignmentid = 1L;
        String newtitle = "new assignment";
        String newdescription = "new description";
        LocalDate newdeadline = LocalDate.of(2025, 1, 31);
        Assignment mockassignment = new Assignment();
        when(assignmentrepo.findById(assignmentid)).thenReturn(Optional.of(mockassignment));
        when(assignmentrepo.save(any(Assignment.class))).thenReturn(mockassignment);

        Assignment updateassignment = assignmentservice.updateassignment(assignmentid, newtitle, newdescription, newdeadline);

        assertNotNull(updateassignment);
        assertEquals(newtitle, updateassignment.getTitle());
        assertEquals(newdescription, updateassignment.getDescription());
        assertEquals(newdeadline, updateassignment.getDeadline());
        verify(assignmentrepo, times(1)).findById(assignmentid);
        verify(assignmentrepo, times(1)).save(any(Assignment.class));
    }

    @Test
    public void test_deleteassignment() {
        Long assignmentid = 1L;
        Assignment mockassignment = new Assignment();
        when(assignmentrepo.findById(assignmentid)).thenReturn(Optional.of(mockassignment));
        assignmentservice.deleteassignment(assignmentid);

        verify(assignmentrepo, times(1)).findById(assignmentid);
        verify(assignmentrepo, times(1)).delete(mockassignment);
    }

    @Test
    public void test_getassignmentsbycourseid() {
        Long courseid = 1L;
        Assignment mockassignment1 = new Assignment();
        Assignment mockassignment2 = new Assignment();
        when(assignmentrepo.findByCourse_CourseId(courseid)).thenReturn(List.of(mockassignment1, mockassignment2));

        List<Assignment> assignments = assignmentservice.getassignmentsbycourseid(courseid);

        assertNotNull(assignments);
        assertEquals(2, assignments.size());
        assertTrue(assignments.contains(mockassignment1));
        assertTrue(assignments.contains(mockassignment2));
        verify(assignmentrepo, times(1)).findByCourse_CourseId(courseid);
    }

    @Test
    public void test_getsubmissionfeedbackandgrade() {
        Long submissionid = 1L;
        AssignmentSubmission mocksubmission = new AssignmentSubmission();
        mocksubmission.setGrade(90);
        mocksubmission.setFeedback("good job");

        when(assignmentsubmissionrepo.findById(submissionid)).thenReturn(Optional.of(mocksubmission));

        FeedbackResponse feedbackResponse = assignmentservice.getsubmissionfeedbackandgrade(submissionid);
        assertNotNull(feedbackResponse);
        assertEquals(90, feedbackResponse.getGrade());
        assertEquals("good job", feedbackResponse.getFeedback());
        verify(assignmentsubmissionrepo, times(1)).findById(submissionid);
    }
}

