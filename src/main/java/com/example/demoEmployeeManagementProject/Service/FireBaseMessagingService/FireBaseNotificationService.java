package com.example.demoEmployeeManagementProject.Service.FireBaseMessagingService;

import com.example.demoEmployeeManagementProject.Entity.Notification.NotificationMessage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireBaseNotificationService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public void sendNotificationByToken(NotificationMessage notificationMessage){
        Notification notification=Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .build();

        Message message=Message
                .builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        firebaseMessaging.sendAsync(message);
        System.out.println("Title = " + notificationMessage.getTitle());
        System.out.println("Body = "+ notificationMessage.getBody());
        System.out.println("Data = "+ notificationMessage.getData());
        System.out.println("Successfully send Notification.");

    }
}
