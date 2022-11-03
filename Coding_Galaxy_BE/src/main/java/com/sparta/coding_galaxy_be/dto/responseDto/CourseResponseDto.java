package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseResponseDto {

    private Long course_id;
    private String title;
    private String content;
    private String thumbNail;
    private String video;
    private int price;
    private boolean paycheck;
    private List<ReviewResponseDto> reviewList;
    private Long reviewCount;
    private List<PaymentResponseDto> paymentsList;
}
