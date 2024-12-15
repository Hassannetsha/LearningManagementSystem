package project_software.lms.quiz.Controllers.Quiz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project_software.lms.quiz.DTOs.Quizzes.QuizCreationDTO;
import project_software.lms.quiz.Entities.QuizEntity;
import project_software.lms.quiz.Services.Quizzes.QuizServices;



@RestController
@RequestMapping(path = "/api/Quizzes")
public class QuizController {
    private final QuizServices quizServices;
    @Autowired
    QuizController(QuizServices quizServices){
        this.quizServices = quizServices;
    }
    @GetMapping
	public List<QuizEntity> getAllQuizzes(){
		return quizServices.getAllQuizzes();
	}
    @PostMapping()
    public void addNewQuiz(@RequestBody QuizCreationDTO quiz) {
        quizServices.addNewQuiz(quiz);
    }
    @DeleteMapping(path = "{quizId}")
    public void deleteQuiz(@PathVariable ("quizId") Long quizId){
        quizServices.deleteQuiz(quizId);
    }
}
