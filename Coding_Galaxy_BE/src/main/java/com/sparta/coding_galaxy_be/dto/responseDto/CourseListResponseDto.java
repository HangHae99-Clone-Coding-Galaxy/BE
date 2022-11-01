package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseListResponseDto {

    private Long course_id;
    private String title;
    private String content;
    private String thumbNail;
    private Double starAverage;
    private Long reviewCount;
}
