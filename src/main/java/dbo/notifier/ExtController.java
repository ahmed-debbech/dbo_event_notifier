package dbo.notifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class ExtController {

    @Autowired
    private Service service;

    @CrossOrigin(origins = "*")
    @PostMapping("/html")
    public void html(@RequestBody String name) {
        service.resume(name);
    }
}
