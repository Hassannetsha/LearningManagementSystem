package com.example.demo.repos;
import com.example.demo.Entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface assignmentRepo extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse_Id(Long courseId);
}
