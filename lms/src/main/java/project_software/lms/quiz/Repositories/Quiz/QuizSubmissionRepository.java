package project_software.lms.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.lms.quiz.model.Quiz.QuizEntity;
import project_software.lms.quiz.model.Quiz.QuizSubmission;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    QuizSubmission findByquiz(QuizEntity quizEntity);
    // QuizSubmission findBycourse(Course course);
}
