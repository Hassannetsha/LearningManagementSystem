package org.example.lmsproject.quiz.Repositories.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.lmsproject.quiz.model.Question.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity,Long>{
    QuestionEntity findByid(Long id);
    // QuestionEntity findBycourse(Course course);
}
