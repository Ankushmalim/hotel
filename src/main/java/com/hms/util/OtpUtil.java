package com.hms.util;

import java.security.SecureRandom;

public class OtpUtil {
    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10)); // Generates digits 0-9
        }
        return otp.toString();
    }
}