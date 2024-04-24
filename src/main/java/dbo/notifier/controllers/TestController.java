package dbo.notifier.controllers;

import dbo.notifier.services.IDatabaseApi;
import dbo.notifier.services.ServiceType;
import dbo.notifier.services.UsersManagement;
import dbo.notifier.services.firebase.AppNotificationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

// the annotations below are disabled for not to be used in production
@RestController
@RequestMapping("/test")
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
    @GetMapping("/notif")
    public void notif(){
        appNotificationService.sendNotif(ServiceType.ADULT_SOLO_BUDO);
    }
}
