package org.example.lmsproject.quiz.Repositories.Quiz;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long>{

     QuizEntity findByquizId(Long quizId);
     QuizEntity findByquizName(String quizName);
    QuizEntity findByquestionBank(QuestionBank questionBank);
    List<QuizEntity> findBycourse(Course course);
}
