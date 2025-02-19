package com.carplayPackage.controller;

import com.carplayPackage.model.Home;
import com.carplayPackage.service.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/getHomeByID")
    public String getHomeByID(@RequestParam(name = "id") Integer id) {
        Optional<Home> home = homeService.getHomeById(id);
        if (home.isPresent()) {
            return home.get().getName();
        } else {
            return "Home not found";
        }
    }
}
