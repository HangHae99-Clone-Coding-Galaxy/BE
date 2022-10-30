package com.sparta.coding_galaxy_be.dto.requestDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayRequestDto {

    private String tid; //단건 결제 고유 코드
    private String next_redirect_pc_url; //요청한 클라이언트가 PC 웹일 경우 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력 화면
    private String partner_order_id;
    private String pg_token;
}
