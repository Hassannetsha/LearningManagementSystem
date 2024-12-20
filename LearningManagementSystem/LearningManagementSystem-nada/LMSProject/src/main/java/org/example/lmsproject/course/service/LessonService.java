package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.Lesson;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.course.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final int OTP_LENGTH = 6;
    private Map<Long, String> lessonotp = new HashMap<>();
    private Map<Long, Long> otpexpiration = new HashMap<>();

    public String generateOtp(Long lessonId) {
        SecureRandom random = new SecureRandom();
        String otp = String.format("%0" + OTP_LENGTH + "d", random.nextInt((int) Math.pow(10, OTP_LENGTH)));
        long expirationTime = System.currentTimeMillis() + 15 * 60 * 1000; // valide for 15 minutes
        lessonotp.put(lessonId, otp);
        otpexpiration.put(lessonId, expirationTime);
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

    // Delete a lesson
    public void deleteLesson(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new IllegalArgumentException("Lesson not found");
        }
        lessonRepository.deleteById(lessonId);
    }


}
