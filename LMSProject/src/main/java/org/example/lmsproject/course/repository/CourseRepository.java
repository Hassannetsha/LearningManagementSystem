package org.example.lmsproject.course.repository;

import org.example.lmsproject.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    boolean existsByTitle(String title);
}
