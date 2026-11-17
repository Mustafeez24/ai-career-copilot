package com.aicareercopilot.profile;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        return Map.of("success", true, "email", authentication.getName());
    }
}