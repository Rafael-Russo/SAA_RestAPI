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

    @PostMapping("/register")
    public UserEntity register(@RequestBody UserEntity request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity request) {
        String token = authService.validateUser(request.getUsername(), request.getPassword());
        return token;
    }

    @GetMapping("/info/{token}")
    public String extractInfos(@PathVariable String token) {
        String infos = authService.extractInfos(token);
        return infos;
    }

    @GetMapping("/user")
    public String getUser(Authentication authentication){
        return "User: " + authentication.getName();
    }

    @Secured({"ROLE_MOD", "ROLE_ADMIN"})
    @PutMapping("/edit/{id}")
    public UserEntity atualizar(@PathVariable String id, @RequestBody UserEntity user) {
        return authService.atualizar(id, user);
    }

    @Secured({"ROLE_MOD", "ROLE_ADMIN"})
    @GetMapping("/mod")
    public String getModerator(Authentication authentication){
        return "Moderator: " + authentication.getName();
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/remove/{id}")
    public void excluir(@PathVariable String id) {
        authService.excluir(id);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String getAdmin(Authentication authentication){
        return "Admin: " + authentication.getName();
    }
}
