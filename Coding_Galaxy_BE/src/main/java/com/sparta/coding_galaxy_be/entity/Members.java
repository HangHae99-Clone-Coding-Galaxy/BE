package com.sparta.coding_galaxy_be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long MemberId;

    @Column(unique = true)
    private Long kakaoMemberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String profileImage;

    @Column(nullable = false)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void editMyProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void editMyNickname(String nickname) {
        this.nickname = nickname;
    }

}
