package com.midziklabs.analytics.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {
    @Value("classpath:google-services.json")
    private Resource private_key;
    private InputStream input_stream;

    @PostConstruct
    public void initialize() throws IOException {
        this.input_stream = new ByteArrayInputStream(private_key.getContentAsByteArray());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(input_stream))
            .build();
        FirebaseApp.initializeApp(firebaseOptions);
    }
     
    

}
