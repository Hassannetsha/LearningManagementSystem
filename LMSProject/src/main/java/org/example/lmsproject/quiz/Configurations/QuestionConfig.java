// package com.lms.project_advanced_software.quiz.Configurations;

// import java.util.List;

// // import org.aspectj.weaver.patterns.TypePatternQuestions;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.lms.project_advanced_software.quiz.Entities.Question.QuestionDTO;
// // import com.lms.project_advanced_software.quiz.Repositories.Question.QuestionRepository;
// import com.lms.project_advanced_software.quiz.Services.Question.QuestionServices;
// @Configuration
// public class QuestionConfig {
//     @Bean
//     CommandLineRunner commandLineRunner(QuestionServices questionServices){
//         return arg->{
//             QuestionDTO questionDTO = new QuestionDTO();
//             questionDTO.setType("MCQ");
//             questionDTO.setQuestionText("What is the capital of France?");
//             questionDTO.setOptions(List.of("Paris", "Berlin", "London"));
//             questionDTO.setCorrectOption("paris");
//             questionServices.addNewQuestion(questionDTO);
//             // QuizEntity quiz1 = new QuizEntity(1,"combinatorics"),quiz2 = new QuizEntity(2,"algebra");
//             // quizRepository.saveAll(List.of(quiz1,quiz2));
//         };
//     }
// }
