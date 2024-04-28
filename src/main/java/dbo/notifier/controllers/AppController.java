package dbo.notifier.controllers;

import dbo.notifier.dto.NewsMessage;
import dbo.notifier.dto.NextEventDto;
import dbo.notifier.dto.ScheduledEvents;
import dbo.notifier.services.IDatabaseApi;
import dbo.notifier.services.IEventsService;
import dbo.notifier.utils.ResultRetreiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("app")
@CrossOrigin(origins = "*")
public class AppController {

    @Autowired
    private IDatabaseApi databaseApi;

    @Autowired
    private IEventsService eventsService;

    @GetMapping("/state")
    public ResponseEntity<ScheduledEvents> next(){

        ScheduledEvents sc = eventsService.getState();
        return new ResponseEntity<>(sc ,HttpStatus.OK);
    }


    @PostMapping("/news")
    public ResponseEntity<Boolean> n(@RequestBody NewsMessage newsMessage){
        boolean b = databaseApi.addNews(newsMessage);
        return new ResponseEntity<>(b , HttpStatus.OK);
    }
    @GetMapping("/news")
    public ResponseEntity<List<NewsMessage>> m(){
        int pid = databaseApi.getNews();
        List<NewsMessage> ff = (List<NewsMessage>) ResultRetreiver.getInstance().waitFor(pid);
        return new ResponseEntity<>(ff ,HttpStatus.OK);
    }
}
