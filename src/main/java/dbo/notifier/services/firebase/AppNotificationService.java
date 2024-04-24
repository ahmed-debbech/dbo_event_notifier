package dbo.notifier.services.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import dbo.notifier.services.ServiceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class AppNotificationService {

    @Value("${firebase.path}")
    private String path;

    public void init(){
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://dbog-c2d7a-default-rtdb.europe-west1.firebasedatabase.app/")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);
    }

    public void sendNotif(ServiceType serviceType, List<String> userIds){


        List<String> registrationTokens = userIds;

        MulticastMessage message = MulticastMessage.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .addAllTokens(registrationTokens)
                .build();

        BatchResponse response = null;
        try {
            response = FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Successfully sent message: " + response.getSuccessCount());
    }
}
