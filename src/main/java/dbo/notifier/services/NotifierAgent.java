package dbo.notifier.services;

import dbo.notifier.logger.LogNotifierAgent;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.enumeration.ServiceType;
import dbo.notifier.services.firebase.FirebaseNotificationService;
import dbo.notifier.services.firebase.IDatabaseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class NotifierAgent implements INotifier {

    LogNotifierAgent out = new LogNotifierAgent();

    @Value("${telegram.url}")
    private String urlTelegram;

    @Autowired
    private IDatabaseApi database;
    @Autowired
    private FirebaseNotificationService appNotificationService;

    @Override
    public void broadcastNotificationThroughEveryMedium(ServiceType serviceType) {
        if(serviceType.equals(ServiceType.EVENT)) return;

        switch (serviceType){
            case WORLD_BOSS:
                    worldboss();
                break;
            case NEWS:
                break;
        }
    }
    @Override
    public void broadcastNotificationThroughEveryMedium(ServiceType serviceType, EventType eventType) {
        if(serviceType.equals(ServiceType.EVENT))
            liveEvents(eventType);
    }

    private void liveEvents(EventType event){
        out.log("Notifying users: FOR EVENT: " + event.name());
        RestTemplate restTemplate = new RestTemplate();
        String url = urlTelegram;
        try {
            url += URLEncoder.encode(event.name() + " event is starting now...", StandardCharsets.UTF_8);
            restTemplate.getForEntity(url, String.class);
            database.addNewEvent(event.name(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "000");
            appNotificationService.sendNotif(ServiceType.EVENT, event);
        } catch (Exception e) {
            out.log("something went wrong while sending to telegram or adding to DB or sending notif through firebase");
        }
    }
    private void worldboss(){
        out.log("Notifying users: world boss is 95%");
        RestTemplate restTemplate = new RestTemplate();
        String url = urlTelegram;
        try {
            url += URLEncoder.encode("Hurry up World boss is 95 percent. get ready.", StandardCharsets.UTF_8);
            restTemplate.getForEntity(url, String.class);
            database.addNewWorldBoss(String.valueOf(new Date().getTime()));
            appNotificationService.sendNotif(ServiceType.WORLD_BOSS);
        } catch (Exception e) {
            out.log("something went wrong while sending to telegram or adding to DB or sending notif through firebase for world boss");
        }
    }
}
