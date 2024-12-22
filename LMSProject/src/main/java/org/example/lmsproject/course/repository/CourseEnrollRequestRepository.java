package org.example.lmsproject.course.repository;

import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollRequestRepository extends JpaRepository<CourseEnrollRequest, Long> {
}
