// package project_software.main.quiz.Configurations;

// // import java.util.List;

// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import project_software.main.quiz.Entities.QuizEntity;
// import project_software.main.quiz.Repositories.QuizRepository;

// // import com.lms.project_advanced_software.quiz.Repositories.QuizRepository;

// @Configuration
// public class QuizConfig {
//     @Bean
//     CommandLineRunner commandLineRunner(QuizRepository quizRepository){
//         return arg->{
//             QuizEntity quiz1 = new QuizEntity(),quiz2 = new QuizEntity();
//             quiz1.setCourseId(1);
//             quiz1.setQuizName("math");
//             quiz2.setCourseId(2);
//             quiz2.setQuizName("combinatorics");
//             quizRepository.saveAll(List.of(quiz1,quiz2));
//         };
//     }
// }
