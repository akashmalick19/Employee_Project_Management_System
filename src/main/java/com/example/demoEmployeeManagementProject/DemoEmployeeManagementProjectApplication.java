package com.example.demoEmployeeManagementProject;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@SpringBootApplication
public class DemoEmployeeManagementProjectApplication {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		GoogleCredentials googleCredentials=GoogleCredentials
				.fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream());
		FirebaseOptions firebaseOptions=FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app=FirebaseApp.initializeApp(firebaseOptions,"my-app");
		return FirebaseMessaging.getInstance(app);
	}
	public static void main(String[] args) throws IOException {

//		ClassLoader classLoader=DemoEmployeeManagementProjectApplication.class.getClassLoader();
//		File file=new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
//		FileInputStream serviceAccount=new FileInputStream(file.getAbsoluteFile());
//
//		FirebaseOptions options = new FirebaseOptions.Builder()
//				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
//				.build();
//
//		FirebaseApp.initializeApp(options);

		SpringApplication.run(DemoEmployeeManagementProjectApplication.class, args);
	}

}