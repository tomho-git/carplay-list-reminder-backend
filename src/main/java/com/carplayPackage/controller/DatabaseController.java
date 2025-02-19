package com.carplayPackage.controller;

import com.carplayPackage.service.DatabaseService;
import com.carplayPackage.service.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import com.carplayPackage.model.Home;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private HomeService homeService;

    @GetMapping("/time")
    public String getCurrentTime() {
        return databaseService.getCurrentTime();
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Hello World!";
    }

}
