package dbo.notifier.services.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import dbo.notifier.logger.LogFirebase;
import dbo.notifier.model.User;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.enumeration.ServiceType;
import dbo.notifier.services.UsersManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class FirebaseNotificationService {

    private LogFirebase out = new LogFirebase();

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
        System.out.println("Done sending messages" );
    }

    public void sendNotif(ServiceType serviceType, EventType eventType){
        //TODO notification check before sending

        if(!serviceType.name().equals(ServiceType.EVENT.name())) return;

        out.log("about to send new notification for event " +eventType.name()+ " -----------");
        List<User> userIds = usersManagement.getAllUsers();
        String title = eventType.name() + " event is starting now!";
        String body = "Join the game now to play.";

        if(userIds == null || userIds.isEmpty()) return;

        for(User ids : userIds){
            out.log("sending notification to " + ids);
            if(!usersManagement.notifyFor(eventType, ids.getNotif_config())){
                out.log("user [["+ids.getFcmToken()+"]] is not interested in being notified");
                continue;
            }
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(ids.getFcmToken())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
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
