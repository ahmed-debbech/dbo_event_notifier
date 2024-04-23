package dbo.notifier.controllers;

import dbo.notifier.dto.UserDto;
import dbo.notifier.services.IUsersManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("users")
public class UsersController {

    @Autowired
    private IUsersManagement usersManagement;

    @PostMapping("/refresh")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        //System.err.println(userDto);
        usersManagement.registerOrRefresh(userDto.getFcmToken());
        return new ResponseEntity<>("hey" ,HttpStatus.OK);
    }
}
