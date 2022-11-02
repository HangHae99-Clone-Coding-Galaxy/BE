package com.sparta.coding_galaxy_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.coding_galaxy_be.dto.requestDto.KakaoPayRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.KakaoPayApprovalResponseDto;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.Payments;
import com.sparta.coding_galaxy_be.repository.PaymentRepository;
import com.sparta.coding_galaxy_be.util.Validation;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PaymentRepository paymentRepository;
    private final Validation validation;

    @Value("${admin-key}")
    private String adminKey;

    @Transactional
    public ResponseEntity<?> payment(Long courseId, Members member) {

        Courses course = validation.validateCourse(courseId);

        Payments payment = Payments.builder()
                .paymentId(UUID.randomUUID().toString())
                .itemName(course.getTitle())
                .itemCode(courseId)
                .createdAt(DateTime.now().toString())
                .approvedAt(DateTime.now().toString())
                .amount(course.getPrice())
                .paymentMethodType("test")
                .paycheck(true)
                .member(member)
                .course(course)
                .build();

        paymentRepository.save(payment);

        return new ResponseEntity<>("결제가 완료되었습니다.", HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> paymentReady(Long courseId, Members member) throws JsonProcessingException {

        //상품 관련 내용을 Course 객체에 저장
        Courses course = validation.validateCourse(courseId);

        //Payment 객체 생성하여 결제 관련 정보 저장
        Payments payment = Payments.builder()
                .paymentId(UUID.randomUUID().toString())
                .itemName(course.getTitle())
                .itemCode(courseId)
                .createdAt(null)
                .approvedAt(null)
                .amount(course.getPrice())
                .paymentMethodType(null)
                .paycheck(false)
                .member(member)
                .course(course)
                .build();

        //카카오 페이 서버 (https://kapi.kakao.com/v1/payment/ready) 에 정보 전달
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + adminKey);

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("cid", "TC0ONETIME");
        httpBody.add("partner_order_id", payment.getPaymentId());
        httpBody.add("partner_user_id", member.getNickname());
        httpBody.add("item_name", course.getTitle());
        httpBody.add("item_code", courseId.toString());
        httpBody.add("quantity", "1");
        httpBody.add("total_amount", String.valueOf(course.getPrice()));
        httpBody.add("tax_free_amount", "0");
        httpBody.add("approval_url", "http://localhost:8080/api/mypage/payment");
        httpBody.add("cancel_url", "http://localhost:8080/api/courses/" + courseId);
        httpBody.add("fail_url", "http://localhost:8080/api/courses/" + courseId);

        HttpEntity<MultiValueMap<String, String>> paymentReadyRequest = new HttpEntity<>(httpBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
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
    public ResponseEntity<?> paymentRequest(KakaoPayRequestDto kakaoPayRequestDto, Members member) throws JsonProcessingException {

        //결제 내역을 찾아옴
        Payments payment = validation.validatePayment(kakaoPayRequestDto.getPartner_order_id());

        //카카오 페이 서버(https://kapi.kakao.com/v1/payment/approve) 정보 전달
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + adminKey);

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("cid", "TC0ONETIME");
        httpBody.add("tid", kakaoPayRequestDto.getTid());
        httpBody.add("partner_order_id", kakaoPayRequestDto.getPartner_order_id());
        httpBody.add("partner_user_id", member.getNickname());
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
