package org.example.lmsproject.assignment.repository;
import org.example.lmsproject.assignment.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
//    List<Assignment> findBycourseId(Long courseId);
    List<Assignment> findByCourse_CourseId(Long courseId);
}
