package br.newton.SAA_RestAPI.controller;

import br.newton.SAA_RestAPI.model.UserEntity;
import br.newton.SAA_RestAPI.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody UserEntity request) {
        String token = authService.generateToken(request.getUsername());
        return token;
    }

    @GetMapping("/username/{token}")
    public String extractUsername(@PathVariable String token) {
        String username = authService.extractUsername(token);
        return username;
    }

    @GetMapping("/user")
    public String getUser(Authentication authentication){
        return "User: " + authentication.getName();
    }

    @Secured("ADMIN")
    @GetMapping("/admin")
    public String onlyAdmin(Authentication authentication){
        return "User: " + authentication.getName();
    }
}
