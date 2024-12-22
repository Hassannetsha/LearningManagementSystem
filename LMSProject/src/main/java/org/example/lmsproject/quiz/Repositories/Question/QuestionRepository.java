package org.example.lmsproject.quiz.Repositories.Question;

import java.util.List;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity,Long>{
    QuestionEntity findByid(Long id);
    QuestionEntity findByquestion(String question);
    List<QuestionEntity> findBycourse(Course course);
}
