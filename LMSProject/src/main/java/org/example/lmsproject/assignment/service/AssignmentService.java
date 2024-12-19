package org.example.lmsproject.assignment.service;

import org.example.lmsproject.assignment.model.*;
import org.example.lmsproject.assignment.repository.AssignmentRepository;
import org.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {
    @Autowired
    AssignmentRepository assignmentrepo;
    @Autowired
    AssignmentSubmissionRepository submissionrepo;
    @Autowired
    private UserRepository userrepo;

    @Autowired
    private CourseRepository courserepo;

    public Assignment createAssignment(Long courseId, String title, String description, LocalDate deadline) {
        Course course = courserepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(deadline);
        assignment.setCourse(course);

        return assignmentrepo.save(assignment);
    }

    public AssignmentSubmission submitAssignment(Long assignmentid, Long studentid, MultipartFile file) {
        Assignment assignment = assignmentrepo.findById(assignmentid)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        if (LocalDateTime.now().isAfter(assignment.getDeadline().atStartOfDay())) {
            throw new RuntimeException("Deadline for this assignment has passed.");
        }
        User student = userrepo.findById(studentid)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileName(file.getOriginalFilename());
        try {
            submission.setFileContent(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file content", e);
        }
        submission.setsubmissiontime(LocalDateTime.now());

        return submissionrepo.save(submission);
    }


    // Grade a submission
    public AssignmentSubmission gradesubmission(Long submissionid, Integer grade, String feedback) {
        AssignmentSubmission submission = submissionrepo.findById(submissionid)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setGrade(grade);
        submission.setFeedback(feedback);

        return submissionrepo.save(submission);
    }

    public Assignment updateassignment(Long assignmentid, String title, String description, LocalDate deadline) {
        Assignment assignment = assignmentrepo.findById(assignmentid)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(deadline);

        return assignmentrepo.save(assignment);
    }

    public void deleteassignment(Long assignmentId) {
        Assignment assignment = assignmentrepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignmentrepo.delete(assignment);
    }

    public List<Assignment> getassignmentsbycourseid(Long courseId){
        return assignmentrepo.findByCourse_CourseId(courseId);

    }

    public FeedbackResponse getsubmissionfeedbackandgrade(Long submissionId) {
        AssignmentSubmission submission = submissionrepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        return new FeedbackResponse(submission.getFeedback(), submission.getGrade());
    }

}
