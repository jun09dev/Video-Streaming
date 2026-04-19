package com.example.service;

//import ch.qos.logback.core.model.Model;
import org.springframework.ui.Model;
//import com.example.service.dto.TrackDto;
//import com.example.service.service.JamendoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViewController {

//    @Autowired
//    private JamendoService jamendoService;

    @GetMapping()
    public String home() {
        return "index"; // Tìm templates/index.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Tìm templates/register.html
    }

    @GetMapping("/forgot")
    public String forgot() {
        return "forgot"; // Tìm templates/forgot.html
    }

    @GetMapping("/home")
    public String home(Model model) {
//        List<TrackDto> tracks = jamendoService.getTopTracks(12);
//        model.addAttribute("tracks", tracks);
        return "home";
    }

    @GetMapping("/reset_password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile"; // -> templates/profile.html
    }
    @GetMapping("/player")
    public String playerPage() {
        return "player"; // templates/player.html
    }

    @GetMapping("/user")
    public String userPage() {
        return "user"; // templates/user.html
    }
}
