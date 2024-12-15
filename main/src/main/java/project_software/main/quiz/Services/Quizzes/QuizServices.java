package project_software.main.quiz.Services.Quizzes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project_software.main.quiz.DTOs.Quizzes.QuizCreationDTO;
import project_software.main.quiz.Entities.QuizEntity;
import project_software.main.quiz.Entities.Question.QuestionEntity;
import project_software.main.quiz.Repositories.Question.QuestionRepository;
import project_software.main.quiz.Repositories.Quiz.QuizRepository;
@Service
public class QuizServices {
	private final QuizRepository quizRepository;
	private final QuestionRepository questionRepository;
	@Autowired
    public QuizServices(QuizRepository quizRepository,QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
    }

	

    public List<QuizEntity> getAllQuizzes(){
		return quizRepository.findAll();
	}



	public void addNewQuiz(QuizCreationDTO quizCreationDTO) {
		QuizEntity quiz = new QuizEntity();
		List<QuestionEntity> questionsIds = new ArrayList<>();
		String Message = "These question IDS are not found: ";
		for (Long quizId : quizCreationDTO.getQuestionIds()) {
			QuestionEntity questionEntity = questionRepository.findByid(quizId);
			if (questionEntity!=null) {
				questionsIds.add(questionEntity);
			}
			else{
				Message+= quizId.toString() + " ";
			}
		}
		quiz.setCourseId(quizCreationDTO.getCourseId());
		quiz.setQuizName(quizCreationDTO.getQuizName());
		if (!questionsIds.isEmpty()) {
			// quiz.setQuestions(questionsIds);
		}
		if (!Message.equals("These question IDS are not found: ")) {
			throw new IllegalStateException(Message);
		}
		quizRepository.save(quiz);
	}



	public void deleteQuiz(Long quizId) {
		QuizEntity quiz = quizRepository.findByquizId(quizId);
		if (quiz!=null) {
			quizRepository.delete(quiz);
		}else{
			throw new IllegalStateException("No quiz found in this course");
		}
	}
}
