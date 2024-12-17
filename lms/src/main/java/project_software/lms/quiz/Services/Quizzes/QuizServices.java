package project_software.lms.quiz.Services.Quizzes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

// import project_software.lms.quiz.CompositePrimaryKeys.QuestionBankId;

import project_software.lms.quiz.DTOs.Quizzes.QuizCreationDTO;
import project_software.lms.quiz.DTOs.Quizzes.QuizSubmissionDTO;
import project_software.lms.quiz.Entities.Question.MCQQuestionEntity;
// import project_software.lms.quiz.Entities.Question.MCQQuestionEntity;
import project_software.lms.quiz.Entities.Question.QuestionBank;
import project_software.lms.quiz.Entities.Question.QuestionEntity;
import project_software.lms.quiz.Entities.Question.TrueOrFalseQuestionEntity;
import project_software.lms.quiz.Entities.Quiz.FeedBack;
import project_software.lms.quiz.Entities.Quiz.QuizEntity;
import project_software.lms.quiz.Entities.Quiz.QuizSubmission;
import project_software.lms.quiz.Repositories.Question.QuestionBankRepository;
import project_software.lms.quiz.Repositories.Quiz.FeedBackRepository;
import project_software.lms.quiz.Repositories.Quiz.QuizRepository;
import project_software.lms.quiz.Repositories.Quiz.QuizSubmissionRepository;
import project_software.lms.quiz.Services.Question.QuestionServices;

@Service
public class QuizServices {

	private final QuizRepository quizRepository;
	private final QuestionServices questionServices;
	// private final QuestionBankRepository questionBankRepository;
	private final QuizSubmissionRepository quizSubmissionRepository;
	private final FeedBackRepository feedBackRepository;

	@Autowired
	public QuizServices(QuizRepository quizRepository, QuestionServices questionServices,
			QuizSubmissionRepository quizSubmissionRepository, FeedBackRepository feedBackRepository) {
		this.quizRepository = quizRepository;
		this.questionServices = questionServices;
		this.quizSubmissionRepository = quizSubmissionRepository;
		this.feedBackRepository = feedBackRepository;
	}

	public List<QuizEntity> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public void addNewQuiz(QuizCreationDTO quizCreationDTO) {
		QuizEntity quiz = new QuizEntity();
		QuestionBank questionBank = questionServices.findQuestionBankByid(quizCreationDTO.getQuestionBankId());
		// CourseEntity course =
		// courseRepository.findByid(quizSubmissionDTO.getCourseId());
		if (questionBank != null) {
			quiz.setCourseId(quizCreationDTO.getCourseId());
			quiz.setQuizName(quizCreationDTO.getQuizName());
			quiz.setQuestionBank(questionBank);
			quizRepository.save(quiz);
		} else {
			throw new IllegalStateException(
					"There is no question bank with this ID: " + quizCreationDTO.getQuestionBankId());
		}
	}

	public void addNewQuizSubmission(QuizSubmissionDTO quizSubmissionDTO) {
		QuizEntity quiz = quizRepository.findByquizId(quizSubmissionDTO.getQuiz());
		if (quiz != null) {
			for (String ans : quizSubmissionDTO.getAnswers()) {
				System.out.println(ans);
			}
			QuizSubmission quizSubmission = new QuizSubmission(quiz, quizSubmissionDTO.getCourseId(),
					quizSubmissionDTO.getAnswers(), quizSubmissionDTO.getStudentId());
			System.out.println("before save quiz submission");
			quizSubmissionRepository.save(quizSubmission);
			System.out.println("before feedback");
			FeedBack feedBack = new FeedBack(quizSubmission.getQuiz(), quizSubmission.getStudentId());
			feedBack.setGrade(calculateGrade(quizSubmission,feedBack));
			System.out.println(feedBack.toString());
			feedBackRepository.save(feedBack);
		} else {
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

	public int calculateGrade(QuizSubmission quizSubmission, FeedBack feedBack) {
		int totalScore = 0;
		List<QuestionEntity> questions = quizSubmission.getQuiz().getQuestionBank().getQuestions();
		List<String> answers = new ArrayList<>();
		for (int i = 0; i < questions.size(); i++) {
			QuestionEntity question = questions.get(i);
			if (question instanceof MCQQuestionEntity mCQQuestionEntity) {
				String userAnswer = mCQQuestionEntity.getRightAnswer();
				totalScore += question.calculateScore(userAnswer);
				answers.add(mCQQuestionEntity.getRightAnswer());
			} else if (question instanceof TrueOrFalseQuestionEntity trueOrFalseQuestionEntity) {
				Boolean userAnswer = trueOrFalseQuestionEntity.getRightAnswer();
				totalScore += question.calculateScore(userAnswer);
				answers.add(trueOrFalseQuestionEntity.getRightAnswer() == true ? "True" : "False");
			}
		}
		feedBack.setAnswers(answers);
		return totalScore;
	}

	public List<FeedBack> getAllFeedBacks() {
		return feedBackRepository.findAll();
	}
	@Transactional
	public void updateQuiz(Long quizId, String quizName, Long questionBankId) {
		QuizEntity quiz = quizRepository.findByquizId(quizId);
		if (quiz!=null) {
			QuestionBank questionBank = questionServices.findQuestionBankByid(questionBankId);
			if (questionBank!=null) {
				if (!Objects.equals(quiz.getQuestionBank().getId(), questionBankId)) {
					if (!quizName.equals(quiz.getQuizName())) {
						quiz.setQuizName(quizName);
						quiz.setQuestionBank(questionBank);
					}
					else{
						throw new IllegalStateException("The quiz with id: " + quizId + " has the same name");
					}
				}
				else{
					throw new IllegalStateException("The quiz with id: " + quizId + " has the same question Bank id");
				}
			}
			else{
				throw new IllegalStateException("No question Bank found");
			}
		}
		else{
			throw new IllegalStateException("No quiz found");
		}
	}
}
