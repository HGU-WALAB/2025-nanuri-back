package com.walab.nanuri.alarm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FcmInitializer {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.UNSUCCESSFUL_FIREBASE_INITIALIZER);
        }
    }
}
