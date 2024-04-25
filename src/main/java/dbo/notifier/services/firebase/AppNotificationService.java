package dbo.notifier.services.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import dbo.notifier.logger.FBLogger;
import dbo.notifier.services.ServiceType;
import dbo.notifier.services.UsersManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class AppNotificationService {

    private FBLogger out = new FBLogger();

    @Autowired
    private UsersManagement usersManagement;

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

    public void sendNotif(ServiceType serviceType){

        out.log("about to send new notification-----------");
        List<String> userIds = usersManagement.getAllFcm();
        String title = "";
        String body = "";
        switch (serviceType){
            case SURP_BUDO:
                title = "SURPRISE!";
                body = "Surprise adult solo Budokai is starting now! hurry up!";
                break;
            case NEWS:
                title = "You have a new update";
                body = "We have a new update for you in the What's New section.";
                break;
            case WORLD_BOSS:
                title = "World boss is 95%";
                body = "Almost here, come and kill the World Boss.";
                break;
            case ADULT_SOLO_BUDO:
                title = "A new adult solo Budokai";
                body = "A new event is starting in 10 mins";
                break;
        }
        for(String ids : userIds){
            out.log("sending notification to " + ids);
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(title)
                            .build())
                    .setToken(ids)
                    .build();

            String response;
            try {
                // Send the message
                response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Successfully sent message: " + response);
                out.log("done sending notification to " + ids);
            } catch (Exception e) {
                out.log("couldn't send notification to " + ids);
                System.err.println("Error sending message: " + e.getMessage());
            }
        }
        out.log("done sending to all notifications");
        System.out.println("Successfully sent messages" );
    }
}
