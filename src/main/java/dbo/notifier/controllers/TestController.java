package dbo.notifier.controllers;

import dbo.notifier.model.ScheduledEventNames;
import dbo.notifier.services.IScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


// the annotations below are disabled for not to be used in production
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IScrapper scrapper;

    @PostMapping("/state")
    public void state(@RequestBody String html) {
        scrapper.resume(html);
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE));
        System.err.println(scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR));
    }
}