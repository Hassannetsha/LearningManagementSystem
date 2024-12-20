package org.example.lmsproject.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.lmsproject.quiz.model.Quiz.FeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;

@Repository
public interface  FeedBackRepository extends JpaRepository<FeedBack, Long>  {
    FeedBack findByquiz(QuizEntity quizEntity);
    // FeedBack findBystudent(User student);
}
