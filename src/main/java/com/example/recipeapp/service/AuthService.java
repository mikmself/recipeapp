package com.example.recipeapp.service;

import com.example.recipeapp.model.User;
import com.example.recipeapp.repository.UserRepository;
import com.example.recipeapp.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final String SECRET_KEY_STRING = "2e4e761bd55c4ac6088d4ad0fd50386e9c58b10ae454289232952c2eaf19d1061ac610d5ce515808a47a6cb897fb1b1e2392d328ba1fc6c7537f1dbdc92b3c028bbd27fa0b9a0175af0d8ccddbe63f61a427713867803a194cbe472763a605a5513d57fb589536ce99adbb014c9e7829542f1f07f6c38f9901af0fe56f52c0319bd5066fd3b4aa579ab5e7c5027321d27bfd09cc48696212a2e1e24567dd4acb4f5a3a7f93393e7cf6381909627e519640c1fcc0307683e6f2dbfb0672dcd5c2d7d768486858a368a051426609a5e6d905decd7ef07c084a9fb2d17521e919173c318506425f6a5c3cc552f5064948fb6c539f53aa8f86d09ac36483cdd2aaec";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY_STRING));
    @Autowired
    private JwtUtil jwtUtil;
    public String register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }
    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public String login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            Long userId = userOptional.get().getId();
            return jwtUtil.generateToken(userId);
        }
        throw new RuntimeException("Invalid username or password");
    }
    public String updateProfile(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!existingUser.getUsername().equals(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already taken");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(existingUser);
        return "Profile updated successfully!";
    }

}

