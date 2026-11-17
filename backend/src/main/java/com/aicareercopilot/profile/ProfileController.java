package com.aicareercopilot.profile;

import com.aicareercopilot.user.AppUser;
import com.aicareercopilot.user.AppUserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final AppUserRepository users;
    private final CareerProfileRepository profiles;

    public ProfileController(AppUserRepository users, CareerProfileRepository profiles) {
        this.users = users;
        this.profiles = profiles;
    }

    public record ProfileRequest(
        @Size(max = 120) String targetRole,
        @Size(max = 80) String experience,
        @Size(max = 1000) String skills
    ) {}

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        AppUser user = findUser(authentication);
        CareerProfile profile = profiles.findByUser_Id(user.getId()).orElse(null);
        return profileResponse(user, profile);
    }

    @PutMapping("/me")
    public Map<String, Object> update(
        Authentication authentication,
        @Valid @RequestBody ProfileRequest request
    ) {
        AppUser user = findUser(authentication);
        CareerProfile profile = profiles.findByUser_Id(user.getId())
            .orElseGet(() -> new CareerProfile(user));
        profile.update(request.targetRole(), request.experience(), request.skills());
        return profileResponse(user, profiles.save(profile));
    }

    private AppUser findUser(Authentication authentication) {
        return users.findByEmailIgnoreCase(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Map<String, Object> profileResponse(AppUser user, CareerProfile profile) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("targetRole", profile == null ? null : profile.getTargetRole());
        details.put("experience", profile == null ? null : profile.getExperience());
        details.put("skills", profile == null ? null : profile.getSkills());
        details.put("updatedAt", profile == null ? null : profile.getUpdatedAt());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("email", user.getEmail());
        result.put("fullName", user.getFullName());
        result.put("profile", details);
        return result;
    }
}