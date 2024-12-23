package org.example.lmsproject.quiz.Repositories.Question;


import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long>{
    
    QuestionBank findByid(Long id);
    QuestionBank findBycourse(Course course);
}
