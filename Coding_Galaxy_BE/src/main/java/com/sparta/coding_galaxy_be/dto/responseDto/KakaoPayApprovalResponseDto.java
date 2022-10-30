package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayApprovalResponseDto {

    private String item_name;
    private String item_code;
    private String created_at; //부가세 금액
    private String approved_at; //사용한 포인트 금액
    private int amount; //총 금액
    private String payment_method_type;
}
