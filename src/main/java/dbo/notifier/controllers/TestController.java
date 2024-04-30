package dbo.notifier.controllers;

import dbo.notifier.model.ScheduledEventNames;
import dbo.notifier.services.ILiveEvents;
import dbo.notifier.services.IScrapper;
import dbo.notifier.services.LiveEvents;
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
}