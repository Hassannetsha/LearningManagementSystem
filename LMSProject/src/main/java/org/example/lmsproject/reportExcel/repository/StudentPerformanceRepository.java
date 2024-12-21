package org.example.lmsproject.reportExcel.repository;

import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentPerformanceRepository extends JpaRepository<StudentPerformance, Long> {

    // Custom query to get student performance by courseId
//    List<StudentPerformance> findByCourseId(Long courseId);
}
