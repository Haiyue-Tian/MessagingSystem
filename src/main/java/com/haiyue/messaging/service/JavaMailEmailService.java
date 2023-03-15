package com.haiyue.messaging.service;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class JavaMailEmailService {
    private JavaMailSender javaMailSender;

    public JavaMailEmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendValidationEmail(String recipientEmail, String validationCode) throws MessageServiceException{
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Your Validation Code for Messaging System");
            helper.setTo(recipientEmail);
            helper.setText("Your validation code is: " + validationCode);
            javaMailSender.send(message);
        } catch(Exception e){
            throw new MessageServiceException(Status.FAILED_TO_SEND_EMAIL);
        }
    }
}
