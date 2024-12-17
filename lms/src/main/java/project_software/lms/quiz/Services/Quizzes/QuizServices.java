package project_software.lms.quiz.Services.Quizzes;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import project_software.lms.quiz.CompositePrimaryKeys.QuestionBankId;
import project_software.lms.quiz.DTOs.Quizzes.QuizCreationDTO;
import project_software.lms.quiz.Entities.Question.QuestionBank;
import project_software.lms.quiz.Entities.QuizEntity;
import project_software.lms.quiz.Repositories.Question.QuestionBankRepository;
import project_software.lms.quiz.Repositories.Quiz.QuizRepository;

@Service
public class QuizServices {
	
	private final QuizRepository quizRepository;
	private final QuestionBankRepository questionBankRepository;

	@Autowired
	public QuizServices(QuizRepository quizRepository, QuestionBankRepository questionBankRepository) {
		this.quizRepository = quizRepository;
		this.questionBankRepository = questionBankRepository;
	}

	public List<QuizEntity> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public void addNewQuiz(QuizCreationDTO quizCreationDTO) {
		QuizEntity quiz = new QuizEntity();
		// List<QuestionEntity> questionsIds = new ArrayList<>();
		// String Message = "These question IDS are not found: ";
		// boolean entered = false;
		// System.out.println("entered add function");
		System.out.println(questionBankRepository.getClass());
		QuestionBank questionBank = questionBankRepository.findByid(quizCreationDTO.getQuestionBankId());
		System.out.println("returned a question Bank");
		System.out.println(questionBank);
		// for (QuestionBank bank : questionBank) {
		// System.out.println("entered the loop");
		if (questionBank != null) {
			// QuestionBankId questionBankId = new
			// QuestionBankId(quizCreationDTO.getQuestionBankId(),
			// quizCreationDTO.getCourseId());
			System.out.println("found the question Bank");
			quiz.setCourseId(quizCreationDTO.getCourseId());
			System.out.println("set the course id");
			quiz.setQuizName(quizCreationDTO.getQuizName());
			System.out.println("set the quiz name");
			// quiz.setQuestionBankId(questionBankId);
			// System.out.println("set the question ban name");
			quiz.setQuestionBank(questionBank);
			System.out.println("set the question Bank");
			System.out.println(quiz);
			quizRepository.save(quiz);
			System.out.println("saved the quiz to the data base");
			// entered = true;
			// break;
		}
		// }
		else {
			throw new IllegalStateException(
					"There is no question bank with this ID: " + quizCreationDTO.getQuestionBankId());
			// if (!entered) {
		}
		// }
		// for (Long quizId : quizCreationDTO.getQuestionBankIds()) {
		// QuestionEntity questionEntity = questionRepository.findByid(quizId);
		// if (questionEntity!=null) {
		// questionsIds.add(questionEntity);
		// }
		// else{
		// Message+= quizId.toString() + " ";
		// }
		// }
		// quiz.setCourseId(quizCreationDTO.getCourseId());
		// quiz.setQuizName(quizCreationDTO.getQuizName());
		// if (!questionsIds.isEmpty()) {
		// // quiz.setQuestions(questionsIds);
		// }
		// if (!Message.equals("These question IDS are not found: ")) {
		// throw new IllegalStateException(Message);
		// }
		// quizRepository.save(quiz);
	}

	public void deleteQuiz(Long quizId) {
		QuizEntity quiz = quizRepository.findByquizId(quizId);
		if (quiz != null) {
			quizRepository.delete(quiz);
		} else {
			throw new IllegalStateException("No quiz found in this course");
		}
	}
}
