package com.example.demo.Service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class LessonService {
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

}
