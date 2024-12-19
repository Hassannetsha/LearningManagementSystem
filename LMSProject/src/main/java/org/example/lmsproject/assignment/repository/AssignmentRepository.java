package org.example.lmsproject.assignment.repository;
import org.example.lmsproject.assignment.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse_Id(Long courseId);
}
