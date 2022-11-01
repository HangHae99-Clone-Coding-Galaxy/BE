package com.sparta.coding_galaxy_be.dto.responseDto;

import com.sparta.coding_galaxy_be.entity.Reviews;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseResponseDto {

    private Long courseId;

    private String title;

    private String content;

    private String thumbNail;

    private String video;

    private List<Reviews> reviewList;

    private Long subscribeCount;

    private String nickname;

    private String kakaoMember;

    private double starAverage;
}
