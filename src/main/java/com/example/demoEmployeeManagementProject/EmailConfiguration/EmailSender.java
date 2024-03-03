package com.example.demoEmployeeManagementProject.EmailConfiguration;

import javax.mail.MessagingException;

public interface EmailSender {

    public void sendTextEmail(String toEmail,String body,String subject);
    public void sendTextAndAttachmentEmail(String toEmail,String body,String subject,String attachment) throws MessagingException;
}
