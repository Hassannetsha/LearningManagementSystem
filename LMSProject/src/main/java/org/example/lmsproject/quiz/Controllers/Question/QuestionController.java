package org.example.lmsproject.quiz.Controllers.Question;

import java.util.List;

import org.example.lmsproject.quiz.DTOs.Questions.QuestionBankDTO;
import org.example.lmsproject.quiz.DTOs.Questions.QuestionDTO;
import org.example.lmsproject.quiz.Services.Question.QuestionServices;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import org.example.lmsproject.quiz.DTOs.Questions.QuestionDTO;
// import org.example.lmsproject.quiz.Entities.Question.QuestionEntity;
// import org.example.lmsproject.quiz.Services.Question.QuestionServices;

@RestController
@RequestMapping(path = "/instructor/questions")
public class QuestionController {
    private final QuestionServices questionServices;

    @Autowired
    public QuestionController(QuestionServices questionServices) {
        this.questionServices = questionServices;
    }

    @GetMapping
    public List<QuestionEntity> getAllQuestions() {
        return questionServices.getAllQuestions();
    }

    @GetMapping("/Banks")
    public List<QuestionBank> getAllQuestionBanks() {
        return questionServices.getAllQuestionBanks();
    }

    @PostMapping("/Banks")
    public void addNewQuestionBank(@RequestBody QuestionBankDTO question) {
        questionServices.addNewQuestionBank(question);
    }

    @DeleteMapping(path = "/Banks/{id}")
    public void deleteQuestionBank(@PathVariable("id") Long id) {
        questionServices.deleteQuestionBank(id);
    }

    @PutMapping(path = "/Banks/{id}")
    public void updateQuestionBank(@PathVariable("id") Long id, @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String questionBankName,
            @RequestParam(required = false) List<Long> questionIds) {
                questionServices.updateQuestionBank(id,courseId,questionBankName,questionIds);
        //http://localhost:8080/Banks/123?courseId=456&questionBankName=Bank1&questionIds=1,2,3,4
    }

    @PostMapping
    public void addNewQuestion(@RequestBody QuestionDTO question) {
        questionServices.addNewQuestion(question);
    }

    @DeleteMapping(path = "{id}")
    public void deleteQuestion(@PathVariable("id") Long id) {
        questionServices.deleteQuestion(id);
    }

    @PutMapping(path = "{id}")
    public void updateQuestion(@PathVariable("id") Long id, @RequestParam(required = false) String question,
            @RequestParam(required = false)String rightTfAnswer,
            @RequestParam(required = false) String rightMcqAnswer,@RequestParam(required = false) List<String> answers) {
        questionServices.updateQuestion(id, question, rightTfAnswer, rightMcqAnswer,answers);
    }
}
