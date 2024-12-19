package org.example.lmsproject.course.repository;

import org.example.lmsproject.course.model.Attendance;
import org.example.lmsproject.course.model.Lesson;
import org.example.lmsproject.userPart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByLessonAndStudent(Lesson lesson, User student);

}
