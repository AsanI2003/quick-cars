package com.project.quickcars2.service;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendRentalEmail(String toEmail, String vehicleName, String pickupDate, String dropoffDate);

//    void sendRentalSms(String phoneNumber, String vehicleName, String pickupDate);
}
