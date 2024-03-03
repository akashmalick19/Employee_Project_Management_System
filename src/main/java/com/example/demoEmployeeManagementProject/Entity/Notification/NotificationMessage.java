package com.example.demoEmployeeManagementProject.Entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    String recipientToken;
    String title;
    String body;
    Map<String,String> data;

}
