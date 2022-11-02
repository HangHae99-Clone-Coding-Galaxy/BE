package com.sparta.coding_galaxy_be.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponseDto {

    private String paymentId;
    private String item_name;
    private Long item_code;
    private String created_at;
    private String approved_at;
    private int amount;
    private String payment_method_type;
    private boolean paycheck;
    private String nickname;
}
