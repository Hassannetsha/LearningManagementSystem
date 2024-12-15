package project_software.lms.quiz.Services.Question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project_software.lms.quiz.DTOs.Questions.QuestionDTO;
import project_software.lms.quiz.Entities.Question.MCQQuestionEntity;
import project_software.lms.quiz.Entities.Question.QuestionEntity;
import project_software.lms.quiz.Entities.Question.TrueOrFalseQuestionEntity;
import project_software.lms.quiz.Repositories.Question.QuestionRepository;

@Service
public class QuestionServices {
    @Autowired
    private QuestionRepository questionRepository;
    
    public List<QuestionEntity> getAllQuestions(){
        return questionRepository.findAll();
    }
    public void addNewQuestion(QuestionDTO  question){
        System.out.println(question.toString());
        QuestionEntity questionEntity;
        if ("MCQ".equalsIgnoreCase(question.getType())) {
            MCQQuestionEntity mcq = new MCQQuestionEntity();
            mcq.setType(question.getType());
            mcq.setQuestion(question.getQuestionText());
            mcq.setAnswers(question.getOptions());
            mcq.setRightAnswer(question.getCorrectOption());
            questionEntity = mcq;
            questionRepository.save(questionEntity);
        } else if ("TrueOrFalse".equalsIgnoreCase(question.getType())) {
            System.out.println("entered in tf");
            TrueOrFalseQuestionEntity tf = new TrueOrFalseQuestionEntity();
            tf.setType(question.getType());
            tf.setQuestion(question.getQuestionText());
            tf.setRightAnswers(question.getCorrectAnswer());
            questionEntity = tf;
            questionRepository.save(questionEntity);
        } else {
            // System.out.println("didn't enter");
            throw new IllegalStateException("Invalid question type: " + question.getType());
        }
    }
    public void deleteQuiz(Long id) {
        QuestionEntity questionToDelete = questionRepository.findByid(id);
        if (questionToDelete != null) {
            questionRepository.delete(questionToDelete);
        }
        else{
            throw new IllegalStateException("No question found in this question bank");
        }
    }
    

}
