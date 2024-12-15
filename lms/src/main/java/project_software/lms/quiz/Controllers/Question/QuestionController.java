package project_software.lms.quiz.Controllers.Question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project_software.lms.quiz.DTOs.Questions.QuestionDTO;
import project_software.lms.quiz.Entities.Question.QuestionEntity;
import project_software.lms.quiz.Services.Question.QuestionServices;

// import project_software.lms.quiz.DTOs.Questions.QuestionDTO;
// import project_software.lms.quiz.Entities.Question.QuestionEntity;
// import project_software.lms.quiz.Services.Question.QuestionServices;


@RestController
@RequestMapping(path = "/api/Questions")
public class QuestionController {
    private final QuestionServices questionServices; 
    @Autowired
    public QuestionController(QuestionServices questionServices) {
        this.questionServices = questionServices;
    }
    @GetMapping
    public List<QuestionEntity> getAllQuestions(){
        return questionServices.getAllQuestions();
    }
    @PostMapping
    public void addNewQuestion(@RequestBody QuestionDTO question){
        questionServices.addNewQuestion(question);
    }
    @DeleteMapping(path = "{id}")
    public void deleteQuiz(@PathVariable ("id") Long id){
        questionServices.deleteQuiz(id);
    }

}
