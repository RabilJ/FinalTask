package com.example.bukmacher.Controller;

import com.example.bukmacher.ConfigAndService.AsyncMailSender;
import com.example.bukmacher.Model.User;
import com.example.bukmacher.Model.UserRole;
import com.example.bukmacher.Repository.UserRepository;
import com.example.bukmacher.Repository.UserRoleRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.UUID;

@Controller
public class UserController {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private AsyncMailSender asyncMailSender;

    public UserController(UserRepository userRepository, UserRoleRepository userRoleRepository, AsyncMailSender asyncMailSender) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.asyncMailSender = asyncMailSender;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(String username, String password, String email) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if (userRepository.findByUsername(username) == null) {
            File file = new File("Sylvanas-Windrunner.jpg");
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(false);
            String code = UUID.randomUUID().toString();
            user.setActivationKey(code);
            user.setEmail(email);
            userRepository.save(user);

            UserRole userRole = new UserRole();
            userRole.setUsername(username);
            userRole.setRole("ROLE_USER");
            userRoleRepository.save(userRole);
            System.out.println("Użytkownik dodany");
            asyncMailSender.sendEmailWithAttachment(user.getUsername(),user.getEmail(),"Rejestracja", "<a href=" + '"' + "http://localhost:8080/aktywuj-konto?key=" + user.getActivationKey() + '"' + ">Link</a>","Sylwa",file);
            return "registerSuccess";
        } else {
            return "registerError";
        }
    }

    @GetMapping("/login?logout")
    public String logout() {
        return "home";
    }
    @GetMapping("/aktywuj-konto")
    public String activation(@RequestParam String key) {
        User user = userRepository.findByActivationKey(key);
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
            return "activationSuccess";
        } else {
            return "/";
        }
    }
}
