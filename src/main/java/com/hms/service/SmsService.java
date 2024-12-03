package com.hms.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public String sendSms(String to, String body) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),              // Recipient's phone number
                    new PhoneNumber(fromPhoneNumber), // Twilio phone number
                    body                             // Message body
            ).create();
            return "SMS sent successfully with SID: " + message.getSid();
        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }
}
