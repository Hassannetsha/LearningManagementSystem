package project_software.lms.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

import project_software.lms.quiz.Entities.Quiz.QuizSubmission;


public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    
}
