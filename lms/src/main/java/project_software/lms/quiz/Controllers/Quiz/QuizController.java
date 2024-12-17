package project_software.lms.quiz.Controllers.Quiz;

import java.util.List;

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

import project_software.lms.quiz.DTOs.Quizzes.QuizCreationDTO;
import project_software.lms.quiz.DTOs.Quizzes.QuizSubmissionDTO;
import project_software.lms.quiz.Entities.Question.QuestionBank;
import project_software.lms.quiz.Entities.Quiz.FeedBack;
import project_software.lms.quiz.Entities.Quiz.QuizEntity;
import project_software.lms.quiz.Entities.Quiz.QuizSubmission;
import project_software.lms.quiz.Services.Quizzes.QuizServices;

@RestController
@RequestMapping(path = "/api/Quizzes")
public class QuizController {
    @Autowired
    private QuizServices quizServices;

    @GetMapping
    public List<QuizEntity> getAllQuizzes() {
        return quizServices.getAllQuizzes();
    }

    @PostMapping
    public void addNewQuiz(@RequestBody QuizCreationDTO quiz) {
        quizServices.addNewQuiz(quiz);
    }

    @DeleteMapping(path = "{quizId}")
    public void deleteQuiz(@PathVariable("quizId") Long quizId) {
        quizServices.deleteQuiz(quizId);
    }

    @PutMapping(path = "{quizId}")
    public void updateQuiz(@PathVariable("quizId") Long quizId, @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String quizName,
            @RequestParam(required = false) Long questionBankId) {
                quizServices.updateQuiz(quizId,quizName,questionBankId);
    }

    @PostMapping(path = "/Submissions")
    public void addNewQuizSubmission(@RequestBody QuizSubmissionDTO quizSubmissionDTO) {
        quizServices.addNewQuizSubmission(quizSubmissionDTO);
    }

    @GetMapping(path = "/Submissions")
    public List<QuizSubmission> getAllQuizSubmissions() {
        return quizServices.getAllQuizSubmissions();
    }

    @GetMapping(path = "/Feedbacks")
    public List<FeedBack> getAllFeedBacks() {
        return quizServices.getAllFeedBacks();
    }
}
