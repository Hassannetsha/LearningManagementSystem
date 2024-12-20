package org.example.lmsproject.quiz.Services.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

@Service
public class QuestionServices {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuizServices quizServices;

    public List<QuestionEntity> getAllQuestions() {
        return questionRepository.findAll();
    }

    public void addNewQuestion(QuestionDTO question) {
        System.out.println(question.toString());
        QuestionEntity questionEntity;
        if ("MCQ".equalsIgnoreCase(question.getType())) {
            MCQQuestionEntity mcq = new MCQQuestionEntity();
            mcq.setType(question.getType());
            mcq.setQuestion(question.getQuestionText());
            mcq.setAnswers(question.getOptions());
            mcq.setRightAnswer(question.getCorrectOption());
            mcq.setCourseId(question.getCourseId());
            questionEntity = mcq;
            questionRepository.save(questionEntity);
        } else if ("TrueOrFalse".equalsIgnoreCase(question.getType())) {
            System.out.println("entered in tf");
            TrueOrFalseQuestionEntity tf = new TrueOrFalseQuestionEntity();
            tf.setType(question.getType());
            tf.setQuestion(question.getQuestionText());
            tf.setRightAnswer(question.getCorrectAnswer());
            tf.setCourseId(question.getCourseId());
            questionEntity = tf;
            questionRepository.save(questionEntity);
        } else {
            // System.out.println("didn't enter");
            throw new IllegalStateException("Invalid question type: " + question.getType());
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
                            if (quiz.getQuestionBank()==questionBank) {
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
        questionBank.setCourseId(questionBankDTO.getCourseId());
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
            // if (type != null) {
            //     if (!type.equalsIgnoreCase(questionEntity.getType())) {
            //         QuestionEntity newQuestionEntity;
            //         if (type.equalsIgnoreCase("mcq")) {
            //             MCQQuestionEntity mcqQuestionEntity = new MCQQuestionEntity();
            //             mcqQuestionEntity.setQuestion(questionEntity.getQuestion());
            //             if (answers != null) {
            //                 mcqQuestionEntity.setAnswers(answers);
            //             } else {
            //                 throw new IllegalStateException("Can't change the type without answers for the new type");
            //             }
            //             if (rightMcqAnswer != null) {
            //                 mcqQuestionEntity.setRightAnswer(rightTfAnswer);
            //             } else {
            //                 throw new IllegalStateException(
            //                         "Can't change the type without the right answer for the new type");
            //             }
            //             mcqQuestionEntity.setType(type);
            //             newQuestionEntity = mcqQuestionEntity;
            //         } else if (type.equalsIgnoreCase("TrueOrFalse")) {
            //             TrueOrFalseQuestionEntity trueOrFalseQuestionEntity = new TrueOrFalseQuestionEntity();
            //             trueOrFalseQuestionEntity.setQuestion(questionEntity.getQuestion());
            //             if (rightTfAnswer != null) {
            //                 trueOrFalseQuestionEntity.setRightAnswer(rightTfAnswer.equalsIgnoreCase("true"));
            //             } else {
            //                 throw new IllegalStateException(
            //                         "Can't change the type without right answer for the new type");
            //             }
            //             trueOrFalseQuestionEntity.setType(type);
            //             newQuestionEntity = trueOrFalseQuestionEntity;
            //         } else {
            //             throw new IllegalStateException("The system doesn't support this type of questions");
            //         }
            //         alreadyEntered = true;
            //         deleteQuestion(questionEntity.getId());
            //         questionRepository.save(newQuestionEntity);
            //         questionEntity = questionRepository.findByid(newQuestionEntity.getId());
            //     } else {
            //         throw new IllegalStateException("The question with id: " + id + " has the same question type");
            //     }
            // }
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
        if (questionBank != null) {
            if (courseId != null) {
                if (!Objects.equals(courseId, questionBank.getCourseId())) {
                    questionBank.setCourseId(courseId);
                } else {
                    throw new IllegalStateException("The question bank with id: " + id + " has the same course id");
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
    QuestionBank findByQuestionBankId(Long questionBankId){
        QuestionBank questionBank = questionBankRepository.findByid(questionBankId);
        if (questionBank!=null) {
            return questionBank;
        }
        throw new IllegalStateException("Question not found");
    }   
    // QuestionEntity findByCourse(Course course){
    //     QuestionEntity questionEntity = questionRepository.findBycourse(course);
    //     if (questionEntity!=null) {
    //         return questionEntity;
    //     }
    //     throw new IllegalStateException("no Questions to this course");
    // }
    QuestionEntity findByQuestionId(Long questionId){
        QuestionEntity questionEntity = questionRepository.findByid(questionId);
        if (questionEntity!=null) {
            return questionEntity;
        }
        throw new IllegalStateException("Question not found");
    }
}
