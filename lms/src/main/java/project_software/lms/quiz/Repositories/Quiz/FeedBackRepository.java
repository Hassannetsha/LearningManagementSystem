package project_software.lms.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.lms.quiz.Entities.Quiz.FeedBack;

@Repository
public interface  FeedBackRepository extends JpaRepository<FeedBack, Long>  {
    
}
