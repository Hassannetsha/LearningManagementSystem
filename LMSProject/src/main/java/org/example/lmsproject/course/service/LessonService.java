package org.example.lmsproject.course.service;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.Lesson;
import org.example.lmsproject.course.model.MessageNotification;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.course.repository.LessonRepository;
import org.example.lmsproject.userPart.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    // added Notification Logic
    @Autowired
    private MailboxService mailboxService;
    //

    private static final int OTP_LENGTH = 6;
    private Map<Long, String> lessonotp = new HashMap<>();
    private Map<Long, Long> otpexpiration = new HashMap<>();

    public String generateOtp(Long lessonId) {
        SecureRandom random = new SecureRandom();
        String otp = String.format("%0" + OTP_LENGTH + "d", random.nextInt((int) Math.pow(10, OTP_LENGTH)));
        long expirationTime = System.currentTimeMillis() + 15 * 60 * 1000; // valide for 15 minutes
        lessonotp.put(lessonId, otp);
        otpexpiration.put(lessonId, expirationTime);

        // added Notification Logic //////////////////////////////////////////////////////////////////////////

        //7a get users by getting course from lesson id

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalStateException("Lesson not found"));

        Course course = lesson.getCourse();
        if (course == null) { throw new IllegalStateException("No course associated with this lesson"); }

        List<Long> studentIds = course.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());
        if (studentIds.isEmpty()) { throw new IllegalStateException("No students are enrolled in this course"); }

        //shoofo law 3ayzeen tsheelo el exceptions
        //i will send a string 3ashan mafeesh 1 object atala3 meno Kol el Info, & el OTP sensitive information

        String message = String.format("Your OTP for the lesson '%s' in course '%s' is: %s",
                lesson.getTitle(),
                course.getTitle(),
                otp);
        NotificationAndEmailMapper messageNotification = new MessageNotification(message);
        mailboxService.addBulkNotifications(studentIds, messageNotification);

        /////////////////////////////////////////////////////////////////////////////////////////////////////

        return otp;
    }

    public boolean validateOtp(Long lessonId, String otp) {
        String generatedOtp = lessonotp.get(lessonId);
        Long expirationTime = otpexpiration.get(lessonId);

        if (generatedOtp == null) {
            return false;
        }

        if (expirationTime == null || expirationTime < System.currentTimeMillis()) {
            return false;
        }
        if (!generatedOtp.trim().equals(otp.trim())) {
            return false; 
        }

        return true;
    }

    public Lesson createLesson(Long courseId, Lesson lesson) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }
        lesson.setCourse(courseOpt.get());
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Long lessonId, Lesson updatedLesson) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        existingLesson.setTitle(updatedLesson.getTitle());
        existingLesson.setDescription(updatedLesson.getDescription());
        existingLesson.setDateTime(updatedLesson.getDateTime());
        return lessonRepository.save(existingLesson);
    }

    public void deleteLesson(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new IllegalArgumentException("Lesson not found");
        }
        lessonRepository.deleteById(lessonId);
    }


}
