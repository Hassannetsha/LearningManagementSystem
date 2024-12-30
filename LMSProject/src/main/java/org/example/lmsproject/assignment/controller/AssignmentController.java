package org.example.lmsproject.assignment.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AssignmentController {
    private final AssignmentService assignmentservice;
    private final AssignmentSubmissionRepository submissionrepo;

    @Autowired
    public AssignmentController(AssignmentService assignmentservice, AssignmentSubmissionRepository submissionrepo) {
        this.assignmentservice = assignmentservice;
        this.submissionrepo = submissionrepo;
    }

    @PostMapping("/instructor/assignments/create")
    public ResponseEntity<String> createAssignment(@RequestParam Long courseid, @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Assignment assignment = assignmentservice.createAssignment(courseid, title, description, deadline);
        return ResponseEntity.ok(assignment.toString());
    }

    @GetMapping("/student/getassignmentbycourse")
    public ResponseEntity<String> getassignmentsbycourseid(@RequestParam Long courseid) {
        List<Assignment> assignments = assignmentservice.getassignmentsbycourseid(courseid);
        String message ="";
        for (Assignment assignment : assignments) {
            message+=assignment.toString()+'\n';
        }
        return ResponseEntity.ok(message);
    }

    @PostMapping("/student/submitassignment")
    public ResponseEntity<String> submitassignment(
            @RequestParam Long assignmentid,
            @RequestParam Long studentid,
            @RequestParam("file") MultipartFile file) {
        try {
            AssignmentSubmission submission = assignmentservice.submitAssignment(assignmentid, studentid, file);
            return ResponseEntity.ok(submission.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/instructor/gradesubmission")
    public ResponseEntity<String> gradesubmission(
            @RequestParam Long submissionid,
            @RequestParam Integer grade,
            @RequestParam String feedback,
            @RequestParam Integer total) {
        AssignmentSubmission submission = assignmentservice.gradesubmission(submissionid, grade, total, feedback);
        return ResponseEntity.ok(submission.toString());
    }

    @PutMapping("/instructor/update")
    public ResponseEntity<String> updateAssignment(
            @RequestParam Long assignmentId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Assignment updatedAssignment = assignmentservice.updateassignment(assignmentId, title, description, deadline);
        return ResponseEntity.ok(updatedAssignment.toString());
    }

    @DeleteMapping("/instructor/deleteassignment")
    public ResponseEntity<Void> deleteAssignment(@RequestParam Long assignmentId) {
        assignmentservice.deleteassignment(assignmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/feedback_grade")
    public ResponseEntity<String> getFeedback(@RequestParam Long submissionId) {
        FeedbackResponse feedbackResponse = assignmentservice.getsubmissionfeedbackandgrade(submissionId);
        return ResponseEntity.ok(feedbackResponse.toString());
    }

    @GetMapping("/instructor/getsubmitedfilecontent")
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
