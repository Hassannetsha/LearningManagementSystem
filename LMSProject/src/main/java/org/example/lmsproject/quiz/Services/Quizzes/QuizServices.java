package org.example.lmsproject.quiz.Services.Quizzes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.TextMappers.AutomatedFeedBackNotification;
import org.example.lmsproject.Notification.TextMappers.NewQuizNotification;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.quiz.DTOs.Quizzes.QuizCreationDTO;
import org.example.lmsproject.quiz.DTOs.Quizzes.QuizSubmissionDTO;
import org.example.lmsproject.quiz.Repositories.Quiz.FeedBackRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizRepository;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizSubmissionRepository;
import org.example.lmsproject.quiz.Services.Question.QuestionServices;
import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.quiz.model.Question.TrueOrFalseQuestionEntity;
import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.quiz.model.Quiz.QuizSubmission;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class QuizServices {

	private final QuizRepository quizRepository;
	private final QuestionServices questionServices;
	private final QuizSubmissionRepository quizSubmissionRepository;
	private final FeedBackRepository feedBackRepository;
	private final CourseService courseService;
	private final StudentService studentService;

	// added Notification Logic
	private final MailboxService mailboxService;

	//
	@Autowired
	public QuizServices(
			QuizRepository quizRepository,
			@Lazy QuestionServices questionServices,
			QuizSubmissionRepository quizSubmissionRepository,
			FeedBackRepository feedBackRepository, CourseService courseService, StudentService studentService,
			MailboxService mailboxService) {
		this.quizRepository = quizRepository;
		this.questionServices = questionServices;
		this.quizSubmissionRepository = quizSubmissionRepository;
		this.feedBackRepository = feedBackRepository;
		this.courseService = courseService;
		this.studentService = studentService;
		this.mailboxService = mailboxService;

	}

	public List<QuizEntity> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public QuizEntity addNewQuiz(QuizCreationDTO quizCreationDTO) {
		QuizEntity quiz = new QuizEntity();
		QuestionBank questionBank = questionServices.findQuestionBankByid(quizCreationDTO.getQuestionBankId());
		Course course = courseService.getCourseById(quizCreationDTO.getCourseId());
		if (questionBank != null) {
			if (course != null) {

				quiz.setCourse(course);
			} else {
				throw new IllegalStateException(
						"There is no course with this ID: " + quizCreationDTO.getCourseId());
			}
			quiz.setQuizName(quizCreationDTO.getQuizName());
			quiz.setQuestionBank(questionBank);

			quizRepository.save(quiz);
			List<Long> studentIds = course.getStudents().stream()
					.map(Student::getId)
					.collect(Collectors.toList());
			if (!studentIds.isEmpty()) {
				NotificationAndEmailMapper newQuizNotification = new NewQuizNotification(quiz);
				mailboxService.addBulkNotifications(studentIds, newQuizNotification);
			}
			return quiz;
		} else {
			throw new IllegalStateException(
					"There is no question bank with this ID: " + quizCreationDTO.getQuestionBankId());
		}
	}

	public void addNewQuizSubmission(QuizSubmissionDTO quizSubmissionDTO, String userName) {
		QuizEntity quiz = quizRepository.findByquizId(quizSubmissionDTO.getQuiz());
		if (quiz != null) {
			for (String ans : quizSubmissionDTO.getAnswers()) {
				System.out.println(ans);
			}
			Course course = courseService.getCourseById(quizSubmissionDTO.getCourseId());
			if (course != null) {
				Student student = studentService.findStudentByUsername(userName);
				if (student != null) {
					for (Student student1 : course.getStudents()) {
						if (student1 == student) {
							QuizSubmission quizSubmission = new QuizSubmission(quiz, course,
									quizSubmissionDTO.getAnswers(), student);
							System.out.println("before save quiz submission");
							System.out.println("before feedback");
							AutomatedFeedBack feedBack = new AutomatedFeedBack(quizSubmission.getQuiz(), student);
							feedBack.setGrade(calculateGrade(quizSubmission, feedBack));
							System.out.println(feedBack.toString());
							NotificationAndEmailMapper automatedFeedBack = new AutomatedFeedBackNotification(feedBack);
							quizSubmissionRepository.save(quizSubmission);
							feedBackRepository.save(feedBack);
							mailboxService.addNotification(student.getId(), automatedFeedBack);
							return;
						}
					}
					throw new IllegalStateException("This student is not registered in this course");
				} else {
					throw new IllegalStateException("Student not found");
				}
			} else {
				throw new IllegalStateException("Course not found");
			}
		} else {
			throw new IllegalStateException("No quiz found");
		}
	}

	public void deleteQuiz(Long quizId) {
		// System.out.println("entered delete function");
		QuizEntity quiz = quizRepository.findByquizId(quizId);
		if (quiz != null) {
			List<QuizSubmission> quizSubmissions = findInQuizSubmission(quiz);
			List<AutomatedFeedBack> feedBacks = findInFeedBacks(quiz);
			for (AutomatedFeedBack feedBack : feedBacks) {
				deleteFeedBack(feedBack);
			}
			for (QuizSubmission sub : quizSubmissions) {
				deleteQuizSubmission(sub);
			}
			quizRepository.delete(quiz);
		} else {
			throw new IllegalStateException("No quiz found in this course");
		}
	}

	public List<QuizSubmission> getAllQuizSubmissions() {
		return quizSubmissionRepository.findAll();
	}

	public int calculateGrade(QuizSubmission quizSubmission, AutomatedFeedBack feedBack) {
		int totalScore = 0;
		List<QuestionEntity> questions = quizSubmission.getQuiz().getQuestionBank().getQuestions();
		List<String> answers = new ArrayList<>();
		for (int i = 0; i < questions.size(); i++) {
			QuestionEntity question = questions.get(i);
			String answer = quizSubmission.getAnswers().get(i);
			if (question instanceof MCQQuestionEntity mCQQuestionEntity) {
				// System.out.println(mCQQuestionEntity.calculateScore(answer));
				totalScore += mCQQuestionEntity.calculateScore(answer);
				answers.add(mCQQuestionEntity.getRightAnswer());
			} else if (question instanceof TrueOrFalseQuestionEntity trueOrFalseQuestionEntity) {
				trueOrFalseQuestionEntity.getRightAnswer();
				// System.out.println(trueOrFalseQuestionEntity.calculateScore(answer));
				totalScore += trueOrFalseQuestionEntity.calculateScore(answer);
				answers.add(trueOrFalseQuestionEntity.getRightAnswer() == true ? "True" : "False");
			}
		}
		feedBack.setAnswers(answers);
		return totalScore;
	}

	public List<AutomatedFeedBack> getAllFeedBacks() {
		return feedBackRepository.findAll();
	}

	@Transactional
	public void updateQuiz(Long quizId, Long courseId, String quizName, Long questionBankId) {
		QuizEntity quiz = quizRepository.findByquizId(quizId);
		if (quiz != null) {
			if (quizName != null) {
				if (!quizName.equalsIgnoreCase(quiz.getQuizName())) {
					quiz.setQuizName(quizName);

				} else {
					throw new IllegalStateException("The quiz with id: " + quizId + " has the same name");
				}
			}
			if (questionBankId != null) {
				QuestionBank questionBank = questionServices.findQuestionBankByid(questionBankId);
				if (questionBank != null) {
					if (!Objects.equals(quiz.getQuestionBank().getId(), questionBankId)) {
						quiz.setQuestionBank(questionBank);
					} else {
						throw new IllegalStateException(
								"The quiz with id: " + quizId + " has the same question Bank id");
					}
				} else {
					throw new IllegalStateException("No question Bank found");
				}
			}
			if (courseId != null) {
				Course course = courseService.getCourseById(courseId);
				if (course != null) {
					if (!Objects.equals(quiz.getCourse(), course)) {
						quiz.setCourse(course);
					} else {
						throw new IllegalStateException("The quiz with id: " + quizId + " has the same course id");
					}
				} else {
					throw new IllegalStateException("No course Bank found");
				}
			}
		} else {
			throw new IllegalStateException("No quiz found");
		}
	}

	public QuizEntity findById(Long quizId) {
		return quizRepository.findByquizId(quizId);
	}

	public List<QuizEntity> findByCourseId(long courseId) {
		Course course = courseService.getCourseById(courseId);
		List<QuizEntity> quizEntity = quizRepository.findBycourse(course);
		if (quizEntity != null) {
			return quizEntity;
		}
		throw new IllegalStateException("No quizzes for this course");
	}

	public QuizEntity findByQuestionBank(QuestionBank questionBank) {
		return quizRepository.findByquestionBank(questionBank);
	}

	public List<QuizSubmission> findInQuizSubmission(QuizEntity quizEntity) {
		return quizSubmissionRepository.findByquiz(quizEntity);
	}

	public List<AutomatedFeedBack> findInFeedBacks(QuizEntity quizEntity) {
		return feedBackRepository.findByquiz(quizEntity);
	}

	public void deleteFeedBack(AutomatedFeedBack feedBack) {
		feedBackRepository.delete(feedBack);
	}

	public void deleteQuizSubmission(QuizSubmission quizSubmission) {
		quizSubmissionRepository.delete(quizSubmission);
	}

}
