package com.zholtikov.mail.service;

import com.zholtikov.mail.dto.EMailDto;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Qualifier("getFreeMarkerConfiguration")
    @Autowired
    Configuration fmConfiguration;


    public void sendEmail(EMailDto mail) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mail.getTo());
        msg.setFrom("${spring.mail.username}");
        msg.setSubject(mail.getSubject());
        msg.setText(mail.getContent());
        javaMailSender.send(msg);
    }

}
