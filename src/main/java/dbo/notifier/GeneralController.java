package dbo.notifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("s")
@CrossOrigin(origins = "*")
public class GeneralController {
    @Autowired
    private ScheduledBudokaiService scheduledBudokaiService;

    @CrossOrigin(origins = "*")
    @GetMapping("")
    public ResponseEntity<String> status(){
        if(scheduledBudokaiService.nextEvent == null)
            return new ResponseEntity<>("no event is registred yet" ,HttpStatus.OK);

        String s = "{ next event will be: " + scheduledBudokaiService.nextEvent.toString() + ", next notify will be: " + scheduledBudokaiService.nextNotif.toString() + " }";
       return new ResponseEntity<>(s ,HttpStatus.OK);
    }
}
