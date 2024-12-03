package com.hms.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WhatsAppService {

    @Value("${twilio.whatsapp.from}")
    private String fromWhatsAppNumber;

//    public String sendWhatsAppMessage(String to, String messageBody) {
//        try {
//            Message message = Message.creator(
//                    new PhoneNumber("whatsapp:" + to),       // Recipient's WhatsApp number
//                    new PhoneNumber("whatsapp:" + fromWhatsAppNumber),     // Twilio Sandbox WhatsApp number
//                    messageBody                              // Message content
//            ).create();
//            return "WhatsApp message sent successfully with SID: " + message.getSid();
//        } catch (Exception e) {
//            return "Failed to send WhatsApp message: " + e.getMessage();
//        }
//    }


    public String sendWhatsAppMessageWithPdf(String to, String messageBody, String mediaUrl) {
        try {
            Message message = Message.creator(
                            new PhoneNumber("whatsapp:" + to),         // Recipient's WhatsApp number
                            new PhoneNumber("whatsapp:" + fromWhatsAppNumber), // Twilio WhatsApp number
                            messageBody                                // Message content
                    )
                    .setMediaUrl(List.of(new java.net.URI(mediaUrl))) // PDF URL
                    .create();

            return "WhatsApp message with PDF sent successfully. SID: " + message.getSid();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send WhatsApp message with PDF: " + e.getMessage();
        }
    }

}

