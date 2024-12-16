package com.example.demo.repos;

import com.example.demo.Entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface submissionRepo extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(Long assignmentId);
}
