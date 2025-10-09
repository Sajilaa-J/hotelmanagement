package com.payment_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentSuccessMail(String toEmail, String userName, Long bookingId, double amount) {
        String subject = "Booking and Payment Confirmation";
        String body = "Dear " + userName + ",\n\n"
                + "Your booking (ID: " + bookingId + ") and payment of ₹" + amount + " were successful.\n\n"
                + "Thank you for choosing RS-TECH!\n\n"
                + "Best regards,\nRS-TECH Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
