package org.example.lmsproject.quiz.Repositories.Question;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long>{
    
    QuestionBank findByid(Long id);
    QuestionBank findBycourse(Course course);
    // List<QuestionBank> findByCourseId(Long courseId);
}
