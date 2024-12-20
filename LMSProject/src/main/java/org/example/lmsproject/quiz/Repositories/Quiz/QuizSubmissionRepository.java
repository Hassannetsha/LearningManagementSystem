package org.example.lmsproject.quiz.Repositories.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.quiz.model.Quiz.QuizSubmission;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    QuizSubmission findByquiz(QuizEntity quizEntity);
    QuizSubmission findBycourse(Course course);
}
