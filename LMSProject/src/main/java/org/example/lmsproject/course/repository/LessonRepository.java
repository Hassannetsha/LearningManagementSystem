package org.example.lmsproject.course.repository;

import org.example.lmsproject.course.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
