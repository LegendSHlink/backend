package org.example.web_service_v2.user.profile;

import org.example.web_service_v2.user.entity.User;
import org.example.web_service_v2.user.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
    Optional<Profile> findByUserId(Long userId);
}
