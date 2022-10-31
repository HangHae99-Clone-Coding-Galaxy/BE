package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageResponseDto {

    private String profileImage;
    private String nickname;
}
