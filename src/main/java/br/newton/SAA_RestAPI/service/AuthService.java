package br.newton.SAA_RestAPI.service;

import br.newton.SAA_RestAPI.model.UserEntity;
import br.newton.SAA_RestAPI.repository.UserRepository;
import br.newton.SAA_RestAPI.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String validateUser(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return generateToken(user.getId(), user.getRole());
    }

    public String generateToken(String id, String role){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("role", role);

        String subject = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            subject = objectMapper.writeValueAsString(userInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String token = JwtUtil.generateToken(subject);
        return token;
    }

    public String extractInfos(String token){
        String username = JwtUtil.extractUsername(token);
        return username;
    }

    public UserEntity atualizar(String id, UserEntity newUser) {
        UserEntity existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(newUser.getUsername());
            existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            existingUser.setRole(newUser.getRole());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public void excluir(String id) {
        userRepository.deleteById(id);
    }
}
