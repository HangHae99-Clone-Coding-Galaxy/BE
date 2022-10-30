package com.sparta.coding_galaxy_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.coding_galaxy_be.dto.requestDto.KakaoPayRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.KakaoPayApprovalResponseDto;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoPayService {

    private final CoursesRepository coursesRepository;

    @Value("${client-id}")
    private String kakaoRestApiKey;

    public ResponseEntity<?> paymentReady(String course_id, KakaoMembers kakaoMember) throws JsonProcessingException {
        //유저 정보와 장바구니 정보 불러옴 -> 강의 단건 정보 불러옴

        //장바구니 정보에서 상품, 수량, 가격, 총합을 꺼내옴 -> 상품 관련 내용은 Course 객체에 저장

        Courses courses = coursesRepository.findByid(course_id).orElseThrow(
                () -> new RuntimeException("찾을 수 없는 강의 입니다.")
        );

        Payment payment = Payment.Builder()
                .courses(courses)
                .amount(courses.price)
                .build();

        //카카오 페이 서버 (https://kapi.kakao.com/v1/payment/ready) 에 정보 전달

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + kakaoRestApiKey);

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("cid", "TC0ONETIME");
        httpBody.add("partner_order_id", payment.getId());
        httpBody.add("partner_user_id", kakaoMember.getNickname());
        httpBody.add("item_name", courses.getTitle());
        httpBody.add("item_code", course_id);
        httpBody.add("quantity", "1");
        httpBody.add("total_amount", payment.getAmount());
        httpBody.add("tax_free_amount", "0");
        httpBody.add("approval_url", "APPROVAL_URL");
        httpBody.add("cancel_url", "CANCEL_URL");
        httpBody.add("fail_url", "FAIL_URL");

        HttpEntity<MultiValueMap<String, String>> paymentReadyRequest = new HttpEntity<>(httpBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v1/payment/ready",
                HttpMethod.POST,
                paymentReadyRequest,
                String.class
        );

        //전달 받은 tid, redirect_url, 주문번호를 저장하여 반환함
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        KakaoPayRequestDto kakaoPayRequestDto = KakaoPayRequestDto.builder()
                .tid(jsonNode.get("tid").asText())
                .next_redirect_pc_url(jsonNode.get("next_redirect_pc_url").asText())
                .partner_order_id(course_id)
                .build();

        return new ResponseEntity<>(kakaoPayRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<?> paymentRequest(KakaoPayRequestDto kakaoPayRequestDto, KakaoMembers kakaoMember) throws JsonProcessingException {

        //카카오 페이 서버(https://kapi.kakao.com/v1/payment/approve) 정보 전달

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + kakaoRestApiKey);

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("cid", "TC0ONETIME");
        httpBody.add("tid", kakaoPayRequestDto.getTid());
        httpBody.add("partner_order_id", kakaoPayRequestDto.getPartner_order_id());
        httpBody.add("partner_user_id", kakaoMember.getNickname());
        httpBody.add("pg_token", kakaoPayRequestDto.getPg_token());

        HttpEntity<MultiValueMap<String, String>> paymentReadyRequest = new HttpEntity<>(httpBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v1/payment/approve",
                HttpMethod.POST,
                paymentReadyRequest,
                String.class
        );

        //**

        // 주문 삭제, 결제내역으로 저장 (구현 필요)

        // **

        //전달 받은 결제 내역을 반환함
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        KakaoPayApprovalResponseDto kakaoPayApprovalResponseDto = KakaoPayApprovalResponseDto.builder()
                .item_name(jsonNode.get("item_name").asText())
                .item_code(jsonNode.get("item_code").asText())
                .created_at(jsonNode.get("created_at").asText())
                .approved_at(jsonNode.get("approved_at").asText())
                .amount(jsonNode.get("amount").get("total").asInt())
                .build();

        return new ResponseEntity<>(kakaoPayApprovalResponseDto, HttpStatus.OK);
    }
}
