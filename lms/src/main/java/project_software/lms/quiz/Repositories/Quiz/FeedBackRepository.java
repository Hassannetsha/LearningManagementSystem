package project_software.lms.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.lms.quiz.model.Quiz.FeedBack;
import project_software.lms.quiz.model.Quiz.QuizEntity;

@Repository
public interface  FeedBackRepository extends JpaRepository<FeedBack, Long>  {
    FeedBack findByquiz(QuizEntity quizEntity);
    // FeedBack findBystudent(User student);
}
