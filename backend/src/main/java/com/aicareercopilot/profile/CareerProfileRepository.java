package com.aicareercopilot.profile;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerProfileRepository extends JpaRepository<CareerProfile, Long> {
    Optional<CareerProfile> findByUser_Id(Long userId);
}