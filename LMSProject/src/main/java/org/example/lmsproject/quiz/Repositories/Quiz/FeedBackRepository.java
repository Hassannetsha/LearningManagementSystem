package org.example.lmsproject.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.lmsproject.quiz.model.Quiz.FeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.Student;

@Repository
public interface  FeedBackRepository extends JpaRepository<FeedBack, Long>  {
    FeedBack findByquiz(QuizEntity quizEntity);
    FeedBack findBystudent(Student student);
}
