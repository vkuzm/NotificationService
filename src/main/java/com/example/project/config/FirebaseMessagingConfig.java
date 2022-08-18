package com.example.project.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseMessagingConfig {

  @Value("${firebase.config.filename}")
  private String firebaseConfigFileName;

  @Value("${firebase.config.appname}")
  private String firebaseConfigAppName;

  @Bean
  public FirebaseMessaging firebaseMessaging() throws IOException {
    var googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource(firebaseConfigFileName).getInputStream());

    var firebaseOptions = FirebaseOptions
        .builder()
        .setCredentials(googleCredentials)
        .build();

    var app = FirebaseApp.initializeApp(firebaseOptions, firebaseConfigAppName);
    return FirebaseMessaging.getInstance(app);
  }
}
