package org.example.lmsproject.userPart.service;


import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.repository.InstructorRepository;
import org.springframework.stereotype.Service;


@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository userRepository) {
        this.instructorRepository = userRepository;
    }

    public Instructor findById(long instructorId) {
        return instructorRepository.findById(instructorId).orElse(null);
    }

    public Instructor findByUsername(String instructorUsername) {
        return instructorRepository.findByUsername(instructorUsername).orElse(null);
    }
}

