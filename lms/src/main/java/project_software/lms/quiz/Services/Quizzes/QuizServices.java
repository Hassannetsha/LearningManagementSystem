package project_software.lms.quiz.Services.Quizzes;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import project_software.lms.quiz.CompositePrimaryKeys.QuestionBankId;

import project_software.lms.quiz.DTOs.Quizzes.QuizCreationDTO;
import project_software.lms.quiz.DTOs.Quizzes.QuizSubmissionDTO;
// import project_software.lms.quiz.DTOs.Quizzes.QuizSubmissionDTO;
import project_software.lms.quiz.Entities.Question.QuestionBank;
import project_software.lms.quiz.Entities.Quiz.QuizEntity;
import project_software.lms.quiz.Entities.Quiz.QuizSubmission;
// import project_software.lms.quiz.Entities.Quiz.QuizSubmission;
import project_software.lms.quiz.Repositories.Question.QuestionBankRepository;
import project_software.lms.quiz.Repositories.Quiz.QuizRepository;
import project_software.lms.quiz.Repositories.Quiz.QuizSubmissionRepository;

@Service
public class QuizServices {
	
	private final QuizRepository quizRepository;
	private final QuestionBankRepository questionBankRepository;
	private final QuizSubmissionRepository quizSubmissionRepository;

	@Autowired
	public QuizServices(QuizRepository quizRepository, QuestionBankRepository questionBankRepository,QuizSubmissionRepository quizSubmissionRepository) {
		this.quizRepository = quizRepository;
		this.questionBankRepository = questionBankRepository;
		this.quizSubmissionRepository = quizSubmissionRepository;
	}

	public List<QuizEntity> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public void addNewQuiz(QuizCreationDTO quizCreationDTO) {
		QuizEntity quiz = new QuizEntity();
		QuestionBank questionBank = questionBankRepository.findByid(quizCreationDTO.getQuestionBankId());
		// CourseEntity course = courseRepository.findByid(quizSubmissionDTO.getCourseId());
		if (questionBank != null) {
			quiz.setCourseId(quizCreationDTO.getCourseId());
			quiz.setQuizName(quizCreationDTO.getQuizName());
			quiz.setQuestionBank(questionBank);
			quizRepository.save(quiz);
		}
		else {
			throw new IllegalStateException(
					"There is no question bank with this ID: " + quizCreationDTO.getQuestionBankId());
		}
	}

	public void addNewQuizSubmission(QuizSubmissionDTO quizSubmissionDTO){
		QuizEntity quiz = quizRepository.findByquizId(quizSubmissionDTO.getQuiz());
		// System.out.println(quizSubmissionDTO);
		// System.out.println(quizSubmissionDTO.getStudentId());
		// CourseEntity course = courseRepository.findByid(quizSubmissionDTO.getCourseId());
		// System.out.println("Entered in add");
		if (quiz!=null) {
			QuizSubmission quizSubmission = new QuizSubmission(quiz,quizSubmissionDTO.getCourseId(),quizSubmissionDTO.getAnswers(),quizSubmissionDTO.getStudentId());
			// System.out.println("defined new quiz submission");
			// System.out.println(quizSubmission);
			quizSubmissionRepository.save(quizSubmission);
		}
		else{
			throw new IllegalStateException("No quiz found");
		}
	}

	public void deleteQuiz(Long quizId) {
		QuizEntity quiz = quizRepository.findByquizId(quizId);
		if (quiz != null) {
			quizRepository.delete(quiz);
		} else {
			throw new IllegalStateException("No quiz found in this course");
		}
	}

	public List<QuizSubmission> getAllQuizSubmissions() {
		return quizSubmissionRepository.findAll();
	}
}
