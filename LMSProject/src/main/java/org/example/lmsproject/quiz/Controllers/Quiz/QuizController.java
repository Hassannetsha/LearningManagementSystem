package org.example.lmsproject.quiz.Controllers.Quiz;

import java.security.Principal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsproject.quiz.DTOs.Quizzes.QuizCreationDTO;
import org.example.lmsproject.quiz.DTOs.Quizzes.QuizSubmissionDTO;
import org.example.lmsproject.quiz.Services.Quizzes.QuizServices;
import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.quiz.model.Quiz.QuizSubmission;
import org.example.lmsproject.userPart.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuizController {
    @Autowired
    private QuizServices quizServices;

    @GetMapping("/instructor/quizzes")
    public ResponseEntity<String> getAllQuizzes() {
        List<QuizEntity> quizzes = quizServices.getAllQuizzes();
        return ResponseEntity.ok(quizzes.toString());
    }
    @GetMapping("/student/quizzes/{quizId}")
    public ResponseEntity<String> getQuiz(@PathVariable("quizId") Long quizId) {
        QuizEntity quiz = quizServices.findById(quizId);
        return ResponseEntity.ok(quiz.toString());
    }

    @PostMapping("/instructor/quizzes")
    public ResponseEntity<String> addNewQuiz(@RequestBody QuizCreationDTO quiz) {
        QuizEntity quizEntity = quizServices.addNewQuiz(quiz);
        return ResponseEntity.ok(quizEntity.toString());
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
    public void addNewQuizSubmission(@RequestBody QuizSubmissionDTO quizSubmissionDTO,Principal principal) {
        quizServices.addNewQuizSubmission(quizSubmissionDTO,principal.getName());
    }

    @GetMapping(path = "/instructor/quizzes/Submissions")
    public ResponseEntity<String> getAllQuizSubmissions() {
        List<QuizSubmission> quizSubmissions = quizServices.getAllQuizSubmissions();
        return ResponseEntity.ok(quizSubmissions.toString());
    }

    @GetMapping(path = "/student/quizzes/Feedbacks")
    public ResponseEntity<String> getAllFeedBacks() {
        List<AutomatedFeedBack> automatedFeedBack = quizServices.getAllFeedBacks();
        return ResponseEntity.ok(automatedFeedBack.toString());
    }
}