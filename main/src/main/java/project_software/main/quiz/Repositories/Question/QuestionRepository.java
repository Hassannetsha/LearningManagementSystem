package project_software.main.quiz.Repositories.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.main.quiz.Entities.Question.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity,Long>{
    QuestionEntity findByid(Long id);
}
