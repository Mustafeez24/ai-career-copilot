package com.aicareercopilot.profile;

import com.aicareercopilot.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "career_profiles")
public class CareerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(length = 120)
    private String targetRole;

    @Column(length = 80)
    private String experience;

    @Column(length = 1000)
    private String skills;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    protected CareerProfile() {}

    public CareerProfile(AppUser user) {
        this.user = user;
    }

    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public String getTargetRole() { return targetRole; }
    public String getExperience() { return experience; }
    public String getSkills() { return skills; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void update(String targetRole, String experience, String skills) {
        this.targetRole = targetRole;
        this.experience = experience;
        this.skills = skills;
        this.updatedAt = Instant.now();
    }
}