package org.example.lmsproject.assignment.controller;

import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.assignment.model.FeedbackResponse;
import org.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import org.example.lmsproject.assignment.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentservice;

    @Autowired
    AssignmentSubmissionRepository submissionrepo;

    @PostMapping("/create")
    public ResponseEntity<Assignment> createAssignment(@RequestParam Long courseid, @RequestParam String title,
                                                       @RequestParam String description, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Assignment assignment = assignmentservice.createAssignment(courseid, title, description, deadline);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/course")
    public ResponseEntity<List<Assignment>> getassignmentsbycourseid(@RequestParam Long courseid) {
        List<Assignment> assignments = assignmentservice.getassignmentsbycourseid(courseid);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/submit")
    public ResponseEntity<AssignmentSubmission> submitassignment(
            @RequestParam Long assignmentid,
            @RequestParam Long studentid,
            @RequestParam("file") MultipartFile file) {
        try {
            AssignmentSubmission submission = assignmentservice.submitAssignment(assignmentid, studentid, file);
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/gradesubmission")
    //@PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<AssignmentSubmission> gradesubmission(
            @RequestParam Long submissionid,
            @RequestParam Integer grade,
            @RequestParam String feedback) {
        AssignmentSubmission submission = assignmentservice.gradesubmission(submissionid, grade, feedback);
        return ResponseEntity.ok(submission);
    }
    @PutMapping("/update")
    public ResponseEntity<Assignment> updateAssignment(
            @RequestParam Long assignmentId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Assignment updatedAssignment = assignmentservice.updateassignment(assignmentId, title, description, deadline);
        return ResponseEntity.ok(updatedAssignment);
    }
    @DeleteMapping("/deleteassignment")
    //@PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteAssignment(@RequestParam Long assignmentId) {
        assignmentservice.deleteassignment(assignmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/submissions/feedback_grade")
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<FeedbackResponse> getFeedback(@RequestParam Long submissionId) {
        FeedbackResponse feedbackResponse = assignmentservice.getsubmissionfeedbackandgrade(submissionId);
        return ResponseEntity.ok(feedbackResponse);
    }

    @GetMapping("/submissions/file")
    public ResponseEntity<String> getFileContent(@RequestParam Long submissionId) {
        AssignmentSubmission submission = submissionrepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        byte[] fileContent = submission.getFileContent();
        String fileName = submission.getFileName();
        String fileAsString = null;
        try {
            fileAsString = new String(fileContent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error decoding file content: " + e.getMessage());
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileAsString);
    }

}
