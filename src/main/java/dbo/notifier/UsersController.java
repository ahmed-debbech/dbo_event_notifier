package dbo.notifier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("users")
public class UsersController {
    @PostMapping("/register")
    public ResponseEntity<String> status(@Re){
        return new ResponseEntity<>(s ,HttpStatus.OK);
    }
}
