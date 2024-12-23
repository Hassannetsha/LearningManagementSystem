// package org.example.lmsproject.Notification.TextMappers;

// import java.util.List;

// import org.example.lmsproject.assignment.model.Assignment;
// import org.example.lmsproject.assignment.model.AssignmentSubmission;
// import org.example.lmsproject.course.model.CourseEnrollRequest;
// import org.example.lmsproject.course.model.CourseMaterial;
// import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
// import org.example.lmsproject.quiz.model.Question.QuestionEntity;
// import org.example.lmsproject.quiz.model.Question.TrueOrFalseQuestionEntity;
// import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;
// import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
// import org.example.lmsproject.userPart.model.Request;
// import org.example.lmsproject.userPart.model.Response;

// public class EmailMapper {
//     public static String getSubject(Object message) {
//         if (message instanceof String) {
//             return "Notification";
//         }
//         return "LMS Notification";
//     }

//     public static String getBody(Object message) {
//         if (message instanceof String) {
//             return (String) message;
//         } 

//         return "You have a new notification. Please check the LMS for more details.";
//     }
// }
