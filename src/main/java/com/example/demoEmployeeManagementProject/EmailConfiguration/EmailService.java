package com.example.demoEmployeeManagementProject.EmailConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;

@Service
public class EmailService implements EmailSender{
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendTextEmail(String toEmail, String body, String subject) {

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("akashmalick2022@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email send to "+ Arrays.toString(simpleMailMessage.getTo()) +" email address.");
    }

    @Override
    public void sendTextAndAttachmentEmail(String toEmail, String body, String subject, String attachment) throws MessagingException {

        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom("akashmalick2022@gmail.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystemResource=new FileSystemResource(new File(attachment));
        mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
        javaMailSender.send(mimeMessage);
        System.out.println("Email send to "+ toEmail +" email address."); //Arrays.toString(mimeMessage.getReplyTo())
    }


}
