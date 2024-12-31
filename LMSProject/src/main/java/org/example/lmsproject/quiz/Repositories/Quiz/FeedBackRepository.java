package org.example.lmsproject.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.Student;

import java.util.List;

@Repository
public interface  FeedBackRepository extends JpaRepository<AutomatedFeedBack, Long>  {
    List<AutomatedFeedBack> findByquiz(QuizEntity quizEntity);
    AutomatedFeedBack findBystudent(Student student);

    List<AutomatedFeedBack> findByStudentId(Long studentId);
}
