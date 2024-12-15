// package project_software.lms.quiz.Services;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import project_software.lms.quiz.Entities.QuizEntity;
// import project_software.lms.quiz.Repositories.QuizRepository;
// // import com.lms.project_advanced_software.quiz.Repositories.QuizRepository;
// @Service
// public class QuizServices {
// 	private final QuizRepository quizRepository;
// 	@Autowired
//     public QuizServices(QuizRepository quizRepository) {
//         this.quizRepository = quizRepository;
//     }

	

//     public List<QuizEntity> getAllQuizzes(){
// 		return quizRepository.findAll();
// 	}



// 	public void addNewQuiz(QuizEntity quiz) {
// 		quizRepository.save(quiz);
// 		// System.out.println(quiz);
// 	}



// 	public void deleteQuiz(String quizName, int courseId) {
// 		List<QuizEntity> quizzes = quizRepository.findBycourseId(courseId);
// 		if (quizzes!=null) {
// 			for (QuizEntity quiz : quizzes) {
// 				if (quiz.getQuizName().equals(quizName)) {
// 					quizRepository.delete(quiz);
// 					break;
// 				}
// 			}
// 			throw new IllegalStateException("No quiz found in this course");
// 		}
// 	}
// }
