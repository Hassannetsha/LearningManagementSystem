package project_software.lms.quiz.Repositories.Quiz;


// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.lms.quiz.Entities.QuizEntity;

// import com.lms.project_advanced_software.quiz.Entities.QuizEntity;
@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String>{

     QuizEntity findByquizId(Long quizId);
    
}
