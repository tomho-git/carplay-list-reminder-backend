package com.yourpackage.controller;

import com.yourpackage.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/time")
    public String getCurrentTime() {
        return databaseService.getCurrentTime();
    }

    @GetMapping("/hello")
        public String getHello(){
            return "Hello World!";
        }
    
}
