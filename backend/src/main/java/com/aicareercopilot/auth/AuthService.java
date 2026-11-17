package com.aicareercopilot.auth;

import com.aicareercopilot.security.JwtService;
import com.aicareercopilot.user.AppUser;
import com.aicareercopilot.user.AppUserRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Map<String, Object> register(String fullName, String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
        AppUser user = users.save(new AppUser(
            normalizedEmail, passwordEncoder.encode(password), fullName.trim()
        ));
        return authResponse(user);
    }

    public Map<String, Object> login(String email, String password) {
        AppUser user = users.findByEmailIgnoreCase(email.trim())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return authResponse(user);
    }

    private Map<String, Object> authResponse(AppUser user) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("token", jwtService.generateToken(user.getEmail()));
        result.put("tokenType", "Bearer");
        result.put("email", user.getEmail());
        result.put("fullName", user.getFullName());
        return result;
    }
}