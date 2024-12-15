package project_software.main.quiz.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project_software.main.quiz.Entities.QuizEntity;
import project_software.main.quiz.Services.QuizServices;



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
    public void dataSubmitForm(@RequestBody QuizEntity quiz) {
        quizServices.addNewQuiz(quiz);
    }
    @DeleteMapping(path = "{quizName}/{courseId}")
    public void deleteQuiz(@PathVariable ("quizName") String quizName,@PathVariable ("courseId") int courseId){
        quizServices.deleteQuiz(quizName,courseId);
    }
}
