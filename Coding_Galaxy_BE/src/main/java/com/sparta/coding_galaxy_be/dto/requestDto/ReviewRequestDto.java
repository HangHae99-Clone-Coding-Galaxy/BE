package com.sparta.coding_galaxy_be.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {

    private Long star;
    private String comment;
}
