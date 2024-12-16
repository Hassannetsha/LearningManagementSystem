package com.example.demo.repos;

import com.example.demo.Entities.Attendance;
import com.example.demo.Entities.Lesson;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface attendanceRepo extends JpaRepository<Attendance, Long> {
    Attendance findByLessonAndStudent(Lesson lesson, User student);

}
