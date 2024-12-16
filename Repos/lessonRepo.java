package com.example.demo.repos;

import com.example.demo.Entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface lessonRepo extends JpaRepository<Lesson, Long> {

}
