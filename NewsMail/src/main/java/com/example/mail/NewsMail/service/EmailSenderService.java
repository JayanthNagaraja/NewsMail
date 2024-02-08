package com.example.mail.NewsMail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService {
    private Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

    @Autowired
    private JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendMail(String recipient, String subject, String body){

        LOGGER.info("Creating mail template...");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("senderEmail");
        message.setTo(recipient);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        LOGGER.info("Mail sent!");
    }

}
