//package com.example.demoEmployeeManagementProject.Configuration;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Configuration
//@Service
//public class FireBaseConfiguration {
//    @Bean
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        GoogleCredentials googleCredentials=GoogleCredentials
//                .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream());
//        FirebaseOptions firebaseOptions=FirebaseOptions.builder()
//                .setCredentials(googleCredentials).build();
//        FirebaseApp app=FirebaseApp.initializeApp(firebaseOptions,"my-app");
//        return FirebaseMessaging.getInstance(app);
//    }
//}
