package dbo.notifier.controllers;

import dbo.notifier.dto.NewsMessage;
import dbo.notifier.dto.NextEventDto;
import dbo.notifier.model.FirebaseEvent;
import dbo.notifier.model.FirebaseEvents;
import dbo.notifier.services.IDatabaseApi;
import dbo.notifier.services.ScheduledBudokaiService;
import dbo.notifier.services.WorldBossService;
import dbo.notifier.utils.ResultRetreiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("s")
@CrossOrigin(origins = "*")
public class GeneralController {
    @Autowired
    private ScheduledBudokaiService scheduledBudokaiService;

    @Autowired
    private IDatabaseApi databaseApi;
    @Autowired
    private WorldBossService worldBossService;

    @CrossOrigin(origins = "*")
    @GetMapping("")
    public ResponseEntity<String> status(){
        if(scheduledBudokaiService.nextEvent == null)
            return new ResponseEntity<>("no event is registred yet" ,HttpStatus.OK);

        String s = "{ next event will be: " + scheduledBudokaiService.nextEvent.toString() + ", next notify will be: " + scheduledBudokaiService.nextNotif.toString() + " }";
       return new ResponseEntity<>(s ,HttpStatus.OK);
    }
    @GetMapping("/past_events")
    public ResponseEntity<List<FirebaseEvents>> events(){
        int pid = databaseApi.getAll();
        List<FirebaseEvents> s = (List<FirebaseEvents>)ResultRetreiver.getInstance().waitFor(pid);
        System.err.println(s);
        return new ResponseEntity<>(s ,HttpStatus.OK);
    }
    @GetMapping("/next")
    public ResponseEntity<NextEventDto> next(){
        if(scheduledBudokaiService.nextEvent == null)
            return new ResponseEntity<>(null ,HttpStatus.NOT_FOUND);

        NextEventDto ned = new NextEventDto();
        ned.setNextEvent(String.valueOf(scheduledBudokaiService.nextEvent.getTime()));
        ned.setNextNotification(String.valueOf(scheduledBudokaiService.nextNotif.getTime()));
        return new ResponseEntity<>(ned ,HttpStatus.OK);
    }
    @GetMapping("/past_boss")
    public ResponseEntity<List<String>> d(){
        List<String> boss = worldBossService.getAllBoss();
        return new ResponseEntity<>(boss ,HttpStatus.OK);
    }
    @PostMapping("/news")
    public ResponseEntity<Boolean> n(@RequestBody NewsMessage newsMessage){
        boolean b = databaseApi.addNews(newsMessage);
        return new ResponseEntity<>(b ,HttpStatus.OK);
    }
    @GetMapping("/news")
    public ResponseEntity<List<NewsMessage>> m(){
        int pid = databaseApi.getNews();
        List<NewsMessage> ff = (List<NewsMessage>)ResultRetreiver.getInstance().waitFor(pid);
        return new ResponseEntity<>(ff ,HttpStatus.OK);
    }
}
