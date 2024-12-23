package org.example.lmsproject.quiz.Controllers.Quiz;

import java.util.List;

import org.example.lmsproject.quiz.DTOs.Quizzes.QuizCreationDTO;
import org.example.lmsproject.quiz.DTOs.Quizzes.QuizSubmissionDTO;
import org.example.lmsproject.quiz.Services.Quizzes.QuizServices;
import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.quiz.model.Quiz.QuizSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class QuizController {
    @Autowired
    private QuizServices quizServices;

    @GetMapping("/instructor/quizzes")
    public List<QuizEntity> getAllQuizzes() {
        return quizServices.getAllQuizzes();
    }

    @PostMapping("/instructor/quizzes")
    public QuizEntity addNewQuiz(@RequestBody QuizCreationDTO quiz) {
        return quizServices.addNewQuiz(quiz);
    }

    @DeleteMapping(path = "/instructor/quizzes/{quizId}")
    public void deleteQuiz(@PathVariable("quizId") Long quizId) {
        quizServices.deleteQuiz(quizId);
    }

    @PutMapping(path = "/instructor/quizzes/{quizId}")
    public void updateQuiz(@PathVariable("quizId") Long quizId, @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String quizName,
            @RequestParam(required = false) Long questionBankId) {
        quizServices.updateQuiz(quizId, courseId, quizName, questionBankId);
    }

    @PostMapping(path = "/student/quizzes/Submissions")
    public void addNewQuizSubmission(@RequestBody QuizSubmissionDTO quizSubmissionDTO) {
        quizServices.addNewQuizSubmission(quizSubmissionDTO);
    }

    @GetMapping(path = "/instructor/quizzes/Submissions")
    public List<QuizSubmission> getAllQuizSubmissions() {
        return quizServices.getAllQuizSubmissions();
    }

    @GetMapping(path = "/student/quizzes/Feedbacks")
    public List<AutomatedFeedBack> getAllFeedBacks() {
        return quizServices.getAllFeedBacks();
    }
}
