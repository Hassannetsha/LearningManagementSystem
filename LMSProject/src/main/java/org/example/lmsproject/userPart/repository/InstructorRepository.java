package org.example.lmsproject.userPart.repository;

import org.example.lmsproject.userPart.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor,Long> {
    Optional<Instructor> findById(Long id);

    Optional<Instructor> findByUsername(String instructorEmail);
}
