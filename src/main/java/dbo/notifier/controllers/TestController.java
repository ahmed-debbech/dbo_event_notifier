package dbo.notifier.controllers;

import dbo.notifier.services.ILastEvents;
import dbo.notifier.services.IUsersManagement;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.enumeration.ScheduledEventNames;
import dbo.notifier.services.IScrapper;
import dbo.notifier.services.LiveEvents;
import dbo.notifier.services.enumeration.ServiceType;
import dbo.notifier.services.firebase.FirebaseNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// the annotations below are disabled for not to be used in production
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IScrapper scrapper;
    @Autowired
    private LiveEvents liveEvents;
    @Autowired
    private IUsersManagement uis;
    @Autowired
    private FirebaseNotificationService appNotificationService;
    @Autowired
    private ILastEvents lastEvents;

    @PostMapping("/state")
    public void state(@RequestBody String html) throws Exception {
        scrapper.resume(html);
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR));
    }

    @GetMapping("/notif/{id}")
    public void st1ate(@PathVariable("id") Integer id) throws Exception {
        liveEvents.apiReturnedValue = id;
    }

    @GetMapping("/n/{id}")
    public void ste1ate(@PathVariable("id") int id){
        EventType e = null;
        switch (id){
            case 1:
                e=EventType.BUDO_ADULT_SOLO;
                break;
            case 2:
                e = EventType.DB_SCRAMBLE;
                break;
        }
        appNotificationService.sendNotif(ServiceType.EVENT, e);
    }
    @GetMapping("/n1")
    public Map<ScheduledEventNames, String> steate(){
        return lastEvents.getLastEvents();
    }
}