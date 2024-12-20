package org.example.lmsproject.quiz.Repositories.Quiz;


// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;

// import com.lms.project_advanced_software.quiz.Entities.QuizEntity;
@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String>{

     QuizEntity findByquizId(Long quizId);
     QuizEntity findByquizName(String quizName);
    QuizEntity findByquestionBank(QuestionBank questionBank);
    // QuizEntity findBycourse(Course course);
    // QuizEntity findBystudent(private User student);
}
