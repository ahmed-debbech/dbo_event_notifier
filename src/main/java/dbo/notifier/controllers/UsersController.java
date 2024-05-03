package dbo.notifier.controllers;

import dbo.notifier.dto.SingleNotification;
import dbo.notifier.dto.UserDto;
import dbo.notifier.model.NotifConfig;
import dbo.notifier.services.IScheduledEvents;
import dbo.notifier.services.IUsersManagement;
import dbo.notifier.services.enumeration.EnumUtils;
import dbo.notifier.services.enumeration.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;

@RestController()
@RequestMapping("users")
public class UsersController {

    @Autowired
    private IUsersManagement usersManagement;
    @Autowired
    private IScheduledEvents scheduledEvents;

    @GetMapping("/update_event/{event}")
    public ResponseEntity<Boolean> modify (@PathVariable("event") String event) {

        if(EnumUtils.eventTypeContains(event)) {
            scheduledEvents.setEventOfTheWeek(EventType.valueOf(event));
            return new ResponseEntity<>(true ,HttpStatus.OK);
        }
        scheduledEvents.setEventOfTheWeek(null);
        return new ResponseEntity<>(false ,HttpStatus.NOT_FOUND);
    }
    @PostMapping("/refresh")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        //System.err.println(userDto);
        usersManagement.registerOrRefresh(userDto.getFcmToken());
        return new ResponseEntity<>("hey" ,HttpStatus.OK);
    }
    @PostMapping("/update/notif")
    public ResponseEntity<Boolean> updateNotif(@RequestBody SingleNotification notification){
        boolean b = usersManagement.updateNotif(notification.getUserId(), notification.getFieldName(), notification.getActivated());
        return new ResponseEntity<>(b ,HttpStatus.OK);
    }
    @GetMapping("/notif/{userId}")
    public ResponseEntity<NotifConfig> update (@PathVariable("userId") String userId) {
        NotifConfig nc = usersManagement.getNotificationsConfig(userId);
        if(nc == null) new ResponseEntity<NotifConfig>(nc ,HttpStatus.NOT_FOUND);
        return new ResponseEntity<NotifConfig>(nc ,HttpStatus.OK);
    }
}
