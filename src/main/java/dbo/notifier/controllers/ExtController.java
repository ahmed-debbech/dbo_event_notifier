package dbo.notifier.controllers;

import dbo.notifier.services.IScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class ExtController {

    @Autowired
    private IScrapper scrapper;

    @CrossOrigin(origins = "*")
    @PostMapping("/html")
    public void html(@RequestBody String name) {
        scrapper.resume(name);
    }
}
