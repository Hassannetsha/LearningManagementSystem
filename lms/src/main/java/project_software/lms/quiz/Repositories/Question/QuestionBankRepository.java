package project_software.lms.quiz.Repositories.Question;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.lms.quiz.model.Question.QuestionBank;
@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long>{
    
    QuestionBank findByid(Long id);
    // List<QuestionBank> findByCourseId(Long courseId);
}
