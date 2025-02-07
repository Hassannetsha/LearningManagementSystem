package org.example.lmsproject.quiz.Services.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.service.CourseService;
import org.example.lmsproject.quiz.DTOs.Questions.QuestionBankDTO;
import org.example.lmsproject.quiz.DTOs.Questions.QuestionDTO;
import org.example.lmsproject.quiz.Repositories.Question.QuestionBankRepository;
import org.example.lmsproject.quiz.Repositories.Question.QuestionRepository;
import org.example.lmsproject.quiz.Services.Quizzes.QuizServices;
import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionBank;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.quiz.model.Question.TrueOrFalseQuestionEntity;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class QuestionServices {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuizServices quizServices;
    @Autowired
    private CourseService courseService;

    public List<QuestionEntity> getAllQuestions() {
        return questionRepository.findAll();
    }

    public void addNewQuestion(QuestionDTO question) {
        System.out.println(question.toString());
        QuestionEntity questionEntity;
        Course course = courseService.getCourseById(question.getCourseId());
        if (course != null) {
            if ("MCQ".equalsIgnoreCase(question.getType())) {
                MCQQuestionEntity mcq = new MCQQuestionEntity();
                mcq.setType(question.getType());
                mcq.setQuestion(question.getQuestionText());
                mcq.setAnswers(question.getOptions());
                mcq.setRightAnswer(question.getCorrectOption());
                mcq.setCourse(course);
                questionEntity = mcq;
                questionRepository.save(questionEntity);
            } else if ("TrueOrFalse".equalsIgnoreCase(question.getType())) {
                System.out.println("entered in tf");
                TrueOrFalseQuestionEntity tf = new TrueOrFalseQuestionEntity();
                tf.setType(question.getType());
                tf.setQuestion(question.getQuestionText());
                tf.setRightAnswer(question.getCorrectAnswer());
                tf.setCourse(course);
                questionEntity = tf;
                questionRepository.save(questionEntity);
            } else {
                throw new IllegalStateException("Invalid question type: " + question.getType());
            }

        } else {
            throw new IllegalStateException("Course not found");
        }
    }

    public void deleteQuestion(Long id) {
        QuestionEntity questionToDelete = questionRepository.findByid(id);
        if (questionToDelete != null) {
            List<QuestionBank> questionBanks = questionBankRepository.findAll();
            for (QuestionBank questionBank : questionBanks) {
                if (questionBank.getQuestions().contains(questionToDelete)) {
                    questionBank.removeQuestion(questionToDelete);
                    if (questionBank.getQuestions().isEmpty()) {
                        QuizEntity quiz = quizServices.findByQuestionBank(questionBank);
                        if (quiz.getQuestionBank() == questionBank) {
                            quiz.setQuestionBank(null);
                            quizServices.deleteQuiz(quiz.getQuizId());
                        }
                        questionBankRepository.delete(questionBank);
                    }
                }
            }
            questionRepository.delete(questionToDelete);
        } else {
            throw new IllegalStateException("No question found");
        }
    }

    public List<QuestionBank> getAllQuestionBanks() {
        return questionBankRepository.findAll();
    }

    public void addNewQuestionBank(QuestionBankDTO questionBankDTO) {
        QuestionBank questionBank = new QuestionBank();
        Course course = courseService.getCourseById(questionBankDTO.getCourseId());
        if (course != null) {
            questionBank.setCourse(course);
        } else {
            throw new IllegalStateException("Course not found");
        }
        questionBank.setQuestionBankName(questionBankDTO.getQuestionBankName());
        List<QuestionEntity> validQuestions = new ArrayList<>();
        String invalidIds = "Invalid question IDs: ";

        for (Long questionId : questionBankDTO.getQuestionIds()) {
            QuestionEntity foundQuestion = questionRepository.findByid(questionId);
            if (foundQuestion != null) {
                validQuestions.add(foundQuestion);
            } else {
                invalidIds += questionId + " ";
            }
        }

        if (validQuestions.isEmpty()) {
            throw new IllegalStateException("No valid question IDs provided.");
        }

        if (!invalidIds.equals("Invalid question IDs: ")) {
            questionBank.setQuestions(validQuestions);
            questionBankRepository.save(questionBank);
            invalidIds += " \nAlthough It is saved.";
            throw new IllegalStateException(invalidIds);
        }

        questionBank.setQuestions(validQuestions);
        questionBankRepository.save(questionBank);
    }

    public void deleteQuestionBank(Long id) {
        QuestionBank questionBank = questionBankRepository.findByid(id);
        if (questionBank != null) {
            questionBankRepository.delete(questionBank);
        } else {
            throw new IllegalStateException("No question Bank found");
        }
    }

    public QuestionBank findQuestionBankByid(Long id) {
        return questionBankRepository.findByid(id);
    }

    public QuestionEntity findQuestionByid(Long id) {
        return questionRepository.findByid(id);
    }

    @Transactional
    public void updateQuestion(Long id, String question, String rightTfAnswer, String rightMcqAnswer,
            List<String> answers) {
        QuestionEntity questionEntity = questionRepository.findByid(id);
        Boolean alreadyEntered = false;
        if (questionEntity != null) {
            if (question != null) {
                if (!question.equalsIgnoreCase(questionEntity.getQuestion())) {
                    questionEntity.setQuestion(question);
                } else {
                    throw new IllegalStateException("The question with id: " + id + " has the same question");
                }
            } else if (questionEntity instanceof TrueOrFalseQuestionEntity trueOrFalseQuestionEntity
                    && rightTfAnswer != null && !alreadyEntered) {
                if (!Objects.equals(rightTfAnswer.equalsIgnoreCase("true"),
                        trueOrFalseQuestionEntity.getRightAnswer())) {
                    trueOrFalseQuestionEntity.setRightAnswer(rightTfAnswer.equalsIgnoreCase("true"));
                } else {
                    throw new IllegalStateException(
                            "The question with id: " + id + " has the same question True or false answer");
                }
            } else if (questionEntity instanceof MCQQuestionEntity mcqQuestionEntity && rightMcqAnswer != null
                    && !alreadyEntered) {
                if (!rightMcqAnswer.equalsIgnoreCase(mcqQuestionEntity.getRightAnswer())) {
                    mcqQuestionEntity.setRightAnswer(rightMcqAnswer);
                } else {
                    throw new IllegalStateException(
                            "The question with id: " + id + " has the same question MCQ answer");
                }
            }

        } else {
            throw new IllegalStateException("Invalid question ID: " + id);
        }
    }

    @Transactional
    public void updateQuestionBank(Long id, Long courseId, String questionBankName, List<Long> questionIds) {
        QuestionBank questionBank = questionBankRepository.findByid(id);
        String Message = "Invalid question IDs: ";
        System.out.println("entered");
        if (questionBank != null) {
            if (courseId != null) {
                Course course = courseService.getCourseById(courseId);
                if (course != null) {
                    questionBank.setCourse(course);
                } else {
                    throw new IllegalStateException("Course not found");
                }
            }
            if (questionBankName != null) {
                if (!questionBankName.equalsIgnoreCase(questionBank.getQuestionBankName())) {
                    questionBank.setQuestionBankName(questionBankName);
                } else {
                    throw new IllegalStateException("The question with id: " + id + " has the same question bank name");
                }
            }
            if (questionIds != null) {
                List<QuestionEntity> questions = new ArrayList<>();
                for (Long questionId : questionIds) {
                    QuestionEntity question = questionRepository.findByid(questionId);
                    if (question != null) {
                        questions.add(question);
                    } else {
                        Message += questionId.toString() + " ";
                    }
                }
                if (!questions.isEmpty()) {
                    questionBank.setQuestions(questions);
                } else {
                    throw new IllegalStateException("No valid question IDs provided.");
                }
                if (!Message.equalsIgnoreCase("Invalid question IDs: ")) {
                    questionBank.setQuestions(questions);
                    Message += " \nAlthough It is saved.";
                    throw new IllegalStateException(Message);
                }
            }
        } else {
            throw new IllegalStateException("Invalid question bank ID: " + id);
        }
    }

    public List<QuestionEntity> findQuestionByCourse(Course course) {
        List<QuestionEntity> questionEntity = questionRepository.findBycourse(course);
        if (questionEntity != null) {
            return questionEntity;
        }
        throw new IllegalStateException("no Questions to this course");
    }

    public QuestionBank findQuestionBankByCourse(Course course) {
        QuestionBank questionBank = questionBankRepository.findBycourse(course);
        if (questionBank != null) {
            return questionBank;
        }
        throw new IllegalStateException("Question not found");
    }

    public QuestionEntity findByQuestionText(String question) {
        QuestionEntity questionEntity = questionRepository.findByquestion(question);
        if (questionEntity != null) {
            return questionEntity;
        }
        throw new IllegalStateException("Question not found");
    }
}
