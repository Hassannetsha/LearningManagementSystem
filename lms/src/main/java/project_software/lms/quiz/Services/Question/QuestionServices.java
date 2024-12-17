package project_software.lms.quiz.Services.Question;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project_software.lms.quiz.DTOs.Questions.QuestionBankDTO;
import project_software.lms.quiz.DTOs.Questions.QuestionDTO;
import project_software.lms.quiz.Entities.Question.MCQQuestionEntity;
import project_software.lms.quiz.Entities.Question.QuestionBank;
import project_software.lms.quiz.Entities.Question.QuestionEntity;
import project_software.lms.quiz.Entities.Question.TrueOrFalseQuestionEntity;
import project_software.lms.quiz.Repositories.Question.QuestionBankRepository;
import project_software.lms.quiz.Repositories.Question.QuestionRepository;
// import project_software.lms.quiz.Services.Generations.Generate;

@Service
public class QuestionServices {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    // @Autowired
    // private Generate generate = new Generate();

    public List<QuestionEntity> getAllQuestions() {
        return questionRepository.findAll();
    }

    

    public void addNewQuestion(QuestionDTO question) {
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
            tf.setRightAnswer(question.getCorrectAnswer());
            questionEntity = tf;
            questionRepository.save(questionEntity);
        } else {
            // System.out.println("didn't enter");
            throw new IllegalStateException("Invalid question type: " + question.getType());
        }
    }

    
    public void deleteQuestion(Long id) {
        QuestionEntity questionToDelete = questionRepository.findByid(id);
        if (questionToDelete != null) {
            questionRepository.delete(questionToDelete);
        } else {
            throw new IllegalStateException("No question found");
        }
    }
    
    
    public List<QuestionBank> getAllQuestionBanks() {
        return questionBankRepository.findAll();
    }

    public void addNewQuestionBank(QuestionBankDTO questionBankDTO) {
        QuestionBank questionBank = new QuestionBank();
        // questionBank.setId(generate.generateUniqueId());
        questionBank.setCourseId(questionBankDTO.getCourseId());

        List<QuestionEntity> validQuestions = new ArrayList<>();
        String invalidIds = "Invalid question IDs: ";

        for (Long questionId : questionBankDTO.getQuestionIds()) {
            QuestionEntity foundQuestion = questionRepository.findByid(questionId);
            if (foundQuestion != null) {
                validQuestions.add(foundQuestion);
            } else {
                invalidIds += questionId + " ";
            }
        }

        if (validQuestions.isEmpty()) {
            throw new IllegalStateException("No valid question IDs provided.");
        }

        if (!invalidIds.equals("Invalid question IDs: ")) {
            questionBank.setQuestions(validQuestions);
            questionBankRepository.save(questionBank);
            throw new IllegalStateException(invalidIds);
        }

        questionBank.setQuestions(validQuestions);
        questionBankRepository.save(questionBank);
    }

    public void deleteQuestionBank(Long id) {
        QuestionBank questionBank= questionBankRepository.findByid(id);
        if (questionBank!=null) {
            questionBankRepository.delete(questionBank);
        }
        else{
            throw new IllegalStateException("No question Bank found");
        }
    }
    public QuestionBank findQuestionBankByid(Long id){
        return questionBankRepository.findByid(id);
    }
    // private Long generateUniqueId() {
    //     return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    // }

}
