package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;

@Builder
public class ReviewResponseDto {

    private Long review_id;
    private String nickname;
    private Long star;
    private String comment;
}
