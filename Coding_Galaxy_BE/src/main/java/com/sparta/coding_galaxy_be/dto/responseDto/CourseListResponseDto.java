package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

import java.util.*;

@Getter
@Builder
public class CourseListResponseDto {

    private Long courseId;

    private String title;

    private String content;

    // imageList????
    private List<String> imageList;

    private String video;
}
