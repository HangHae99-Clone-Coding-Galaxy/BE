package com.sparta.coding_galaxy_be.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CourseRequestDto {

    private String title;
    private String content;
    private MultipartFile thumbNail;
    private MultipartFile video;
    private int price;
}
