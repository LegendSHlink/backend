package org.example.web_service_v2.user.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.user.entity.User;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter@Setter
public class Profile {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100)
    private String field;

    @Column(length = 150)
    private String headline;

    private String portfolioLink;
    private String userImageLink;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public static Profile create(User user) {
        Profile p = new Profile();
        p.setUser(user);
        p.setId(user.getId());
        return p;
    }

    public static Profile of(String field,
                             String headline,
                             String portfolioLink,
                             String userImageLink,
                             User user){

        return Profile.builder()
                .id(user.getId())
                .field(field)
                .headline(headline)
                .portfolioLink(portfolioLink)
                .userImageLink(userImageLink)
                .user(user)
                .build();

    }
}
