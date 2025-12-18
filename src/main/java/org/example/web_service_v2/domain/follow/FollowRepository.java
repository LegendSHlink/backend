package org.example.web_service_v2.domain.follow;

import org.example.web_service_v2.domain.follow.entity.Follow;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("select f.following from Follow f where f.follower.id = :profileId")
    List<Profile> findFollowingsFollowerProfileId(Long profileId);

    @Query("select f.follower from Follow f where f.following.id = :profileId")
    List<Profile> findFollowersByFollowingProfileId(Long profileId);


    long countByFollowerId(Long followerProfileId);
    long countByFollowingId(Long followingProfileId);
}
