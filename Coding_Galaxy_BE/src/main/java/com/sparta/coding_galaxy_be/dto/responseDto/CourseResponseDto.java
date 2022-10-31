package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseResponseDto {

    private Long courseId;

    private String title;

    private String content;

    // imageList?
    private List<String> image;

    private String video;

    // reviewList 추가

    private Long subscribeCount;

    private String nickname;
}
