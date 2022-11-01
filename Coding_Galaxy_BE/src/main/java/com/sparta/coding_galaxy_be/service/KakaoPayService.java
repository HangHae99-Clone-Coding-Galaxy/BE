package com.sparta.coding_galaxy_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.coding_galaxy_be.dto.requestDto.KakaoPayRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.KakaoPayApprovalResponseDto;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.Payments;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
import com.sparta.coding_galaxy_be.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;

    @Value("${client-id}")
    private String kakaoRestApiKey;

    @Transactional
    public ResponseEntity<?> paymentReady(Long course_id, KakaoMembers kakaoMember) throws JsonProcessingException {

        //상품 관련 내용을 Course 객체에 저장
        Courses course = courseRepository.findById(course_id).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        //Payment 객체 생성하여 결제 관련 정보 저장
        Payments payment = Payments.builder()
                .itemName(course.getTitle())
                .itemCode(course_id)
                .amount(course.getPrice())
                .kakaoMember(kakaoMember)
                .course(course)
                .build();

        //카카오 페이 서버 (https://kapi.kakao.com/v1/payment/ready) 에 정보 전달
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + kakaoRestApiKey);

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("cid", "TC0ONETIME");
        httpBody.add("partner_order_id", payment.getPaymentId());
        httpBody.add("partner_user_id", kakaoMember.getNickname());
        httpBody.add("item_name", course.getTitle());
        httpBody.add("item_code", course_id.toString());
        httpBody.add("quantity", "1");
        httpBody.add("total_amount", String.valueOf(course.getPrice()));
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
                .partner_order_id(payment.getPaymentId())
                .build();

        paymentRepository.save(payment);

        return new ResponseEntity<>(kakaoPayRequestDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> paymentRequest(KakaoPayRequestDto kakaoPayRequestDto, KakaoMembers kakaoMember) throws JsonProcessingException {

        //결제 내역을 찾아옴
        Payments payment = paymentRepository.findById(Long.parseLong(kakaoPayRequestDto.getPartner_order_id())).orElseThrow(
                () -> new RuntimeException("결제 내역을 찾을 수 없습니다.")
        );

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

        //결제 내역을 저장함
        payment.updatePayment(kakaoPayApprovalResponseDto);
        paymentRepository.save(payment);

        return new ResponseEntity<>(kakaoPayApprovalResponseDto, HttpStatus.OK);
    }
}
