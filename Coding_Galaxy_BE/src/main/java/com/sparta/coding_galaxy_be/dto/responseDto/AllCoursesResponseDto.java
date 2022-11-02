package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AllCoursesResponseDto {

    private List<CourseListResponseDto> courseListResponseDtoList;
    private List<ReviewResponseDto> reviewList;
}
