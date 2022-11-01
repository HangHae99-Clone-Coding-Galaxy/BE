package com.sparta.coding_galaxy_be.dto.requestDto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CourseRequestDto {

    private String title;

    private String content;

    private MultipartFile thumbNail;

    private MultipartFile video;
}
