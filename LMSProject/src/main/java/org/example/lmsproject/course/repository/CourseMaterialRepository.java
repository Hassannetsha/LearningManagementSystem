package org.example.lmsproject.course.repository;

import org.example.lmsproject.course.model.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    CourseMaterial findByFilename(String filename);
}
