package com.tpisoftware.org.stlucia.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping({"", "index"})
    public RedirectView home() {
        return new RedirectView("/product/list");
    }

}