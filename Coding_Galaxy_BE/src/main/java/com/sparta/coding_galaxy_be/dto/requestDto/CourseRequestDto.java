package com.sparta.coding_galaxy_be.dto.requestDto;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseRequestDto {

    private String title;

    private String content;

    // imageList?
    private List<String> image;

    private String video;
}
