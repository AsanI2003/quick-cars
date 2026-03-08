package com.project.quickcars2.service.impl;

import com.project.quickcars2.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Value("${ideamart.sms.url}")
//    private String smsApiUrl;
//
//    @Value("${ideamart.sms.appId}")
//    private String appId;
//
//    @Value("${ideamart.sms.appSecret}")
//    private String appSecret;
//
//    @Value("${ideamart.sms.sender}")
//    private String sender;

    @Override
    public void sendRentalEmail(String toEmail, String vehicleName, String pickupDate, String dropoffDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("QuickCars Rental Confirmation");
        message.setText("Your rental for " + vehicleName + " from " + pickupDate + " to " + dropoffDate + " has been received. We'll contact you soon.");
        mailSender.send(message);
    }

//    @Override
//    public void sendRentalSms(String phoneNumber, String vehicleName, String pickupDate) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, Object> payload = Map.of(
//                "app_id", appId,
//                "app_secret", appSecret,
//                "address", phoneNumber,
//                "message", "QuickCars: Your rental for " + vehicleName + " on " + pickupDate + " is confirmed.",
//                "sender", sender
//        );
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
//        restTemplate.postForEntity(smsApiUrl, request, String.class);
//    }
}
