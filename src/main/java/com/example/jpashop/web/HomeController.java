package com.example.jpashop.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    Logger log = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping()
    public String home() {
        log.info("home controller");
        return "home";
    }

}
