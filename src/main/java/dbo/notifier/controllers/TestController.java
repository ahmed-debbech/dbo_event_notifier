package dbo.notifier.controllers;

import dbo.notifier.services.IDatabaseApi;
import dbo.notifier.services.ServiceType;
import dbo.notifier.services.UsersManagement;
import dbo.notifier.services.firebase.AppNotificationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

// the annotations below are disabled for not to be used in production
//@RestController
//@RequestMapping("/test")
public class TestController {

    @Autowired
    private IDatabaseApi database;
    @Autowired
    private AppNotificationService appNotificationService;


    @PostMapping("/boss")
    public ResponseEntity<Boolean> firebaseAddBoss(){
        database.addNewWorldBoss(String.valueOf(new Date().getTime()));
        return new ResponseEntity<>(true , HttpStatus.OK);
    }
    @PostMapping("/budo")
    public ResponseEntity<Boolean> firebaseBudo(){
        database.addNewEvent("sss", String.valueOf(new Date().getTime()));
        return new ResponseEntity<>(true , HttpStatus.OK);
    }
    @GetMapping("/notif/{type}")
    public void notif(@PathVariable("type") int type){
        if(type==0)
            appNotificationService.sendNotif(ServiceType.ADULT_SOLO_BUDO);
        if(type==1)
            appNotificationService.sendNotif(ServiceType.SURP_BUDO);
        if(type==2)
            appNotificationService.sendNotif(ServiceType.WORLD_BOSS);
        if(type==3)
            appNotificationService.sendNotif(ServiceType.NEWS);

    }
}
