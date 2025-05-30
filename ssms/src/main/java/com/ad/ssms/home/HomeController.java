package com.ad.ssms.home;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin(origins = "http://localhost:4200") // Replace with your allowed origins
@RestController
@RequestMapping("/public")
public class HomeController {
    
    @Autowired
    private HomeService homeService;
    
    @GetMapping("/all")
    public Home getHomeData() {
        return homeService.getHomeData();
    }
}
