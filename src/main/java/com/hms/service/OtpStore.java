package com.hms.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class OtpStore {
    private static final Map<String, String> otpMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> otpExpiryMap = new ConcurrentHashMap<>();


    public static void storeOtp(String phoneNumber, String otp, long expiryTimeMillis) {
        otpMap.put(phoneNumber, otp);
        otpExpiryMap.put(phoneNumber, System.currentTimeMillis() + expiryTimeMillis);
    }

    public static boolean isOtpValid(String phoneNumber, String otp) {
        if (!otpMap.containsKey(phoneNumber)) {
            return false; // OTP not found
        }

        // Check expiry
        long expiryTime = otpExpiryMap.get(phoneNumber);
        if (System.currentTimeMillis() > expiryTime) {
            removeOtp(phoneNumber);
            return false; // OTP expired
        }

        // Validate OTP
        return otp.equals(otpMap.get(phoneNumber));
    }
//
    public static void removeOtp(String phoneNumber) {
        otpMap.remove(phoneNumber);
        otpExpiryMap.remove(phoneNumber);
    }
}

