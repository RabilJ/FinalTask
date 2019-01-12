package com.example.bukmacher.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

@GetMapping("/")
public String home(Model model, Principal principal){
    if(principal!=null) {
        String infoAboutUser = ("Jesteś zalogowany jako: " + principal.getName());
        model.addAttribute("ActiveUser", infoAboutUser);
    }
    return "home";
}
}
