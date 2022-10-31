package com.sparta.coding_galaxy_be.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoMemberInformationDto {

    private Long kakaoMemberId;
    private String email;
    private String profile_image_url;
    private String nickname;
}
