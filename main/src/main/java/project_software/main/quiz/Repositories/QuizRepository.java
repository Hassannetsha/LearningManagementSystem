package project_software.main.quiz.Repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.main.quiz.Entities.QuizEntity;

// import com.lms.project_advanced_software.quiz.Entities.QuizEntity;
@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String>{

     List<QuizEntity> findBycourseId(int courseId);
    
}
