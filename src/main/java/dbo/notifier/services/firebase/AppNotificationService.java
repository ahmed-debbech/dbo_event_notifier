package dbo.notifier.services.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import dbo.notifier.logger.FBLogger;
import dbo.notifier.services.EventType;
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

        if(serviceType.name().equals(ServiceType.EVENT.name())) return;


        out.log("about to send new notification for " +serviceType.name()+ "-----------");
        List<String> userIds = usersManagement.getAllFcm();
        String title = "";
        String body = "";
        switch (serviceType){
            case NEWS:
                title = "You have a new update";
                body = "We have something new for you.";
                break;
            case WORLD_BOSS:
                title = "World boss is 95%";
                body = "Almost there, come and kill the World Boss.";
                break;
        }
        for(String ids : userIds){
            out.log("sending notification to " + ids);
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
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

    public void sendNotif(ServiceType serviceType, EventType eventType){

        if(!serviceType.name().equals(ServiceType.EVENT.name())) return;

        out.log("about to send new notification for event " +eventType.name()+ " -----------");
        List<String> userIds = usersManagement.getAllFcm();
        String title = eventType.name() + " event is starting now!";
        String body = "Join the game now to play.";

        if(userIds == null || userIds.isEmpty()) return;

        for(String ids : userIds){
            out.log("sending notification to " + ids);
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
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
