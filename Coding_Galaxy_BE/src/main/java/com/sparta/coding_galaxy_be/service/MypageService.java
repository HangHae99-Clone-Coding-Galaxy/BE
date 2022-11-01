package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.EditMyInfoRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.MypageResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.PaymentResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.ReviewResponseDto;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.Payments;
import com.sparta.coding_galaxy_be.entity.Reviews;
import com.sparta.coding_galaxy_be.repository.KakaoMembersRepository;
import com.sparta.coding_galaxy_be.repository.PaymentRepository;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final KakaoMembersRepository kakaoMembersRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final S3UploadService s3UploadService;

    public ResponseEntity<?> getMypage(KakaoMembers kakaoMember) {

        MypageResponseDto mypageResponseDto = MypageResponseDto.builder()
                .profileImage(kakaoMember.getProfileImage())
                .nickname(kakaoMember.getNickname())
                .build();

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editMyInfo(EditMyInfoRequestDto editMyInfoRequestDto, KakaoMembers kakaoMember) throws IOException {

        if (editMyInfoRequestDto.getProfileImage() != null) {
            String imageUrl = s3UploadService.uploadImage(editMyInfoRequestDto.getProfileImage());
            kakaoMember.editMyProfileImage(imageUrl);
        }

        if (editMyInfoRequestDto.getNickname() != null) {
            kakaoMember.editMyNickname(editMyInfoRequestDto.getNickname());
        }

        kakaoMembersRepository.save(kakaoMember);

        return new ResponseEntity<>("내 정보 변경 성공", HttpStatus.OK);
    }

    public ResponseEntity<?> getMyPayment(KakaoMembers kakaoMember) {

        List<Payments> paymentList = paymentRepository.findAllByKakaoMember(kakaoMember);
        List<PaymentResponseDto> paymentResponseDtoList = new ArrayList<>();

        for (Payments payment : paymentList) {
            paymentResponseDtoList.add(
                    PaymentResponseDto.builder()
                            .paymentId(payment.getPaymentId().toString())
                            .item_name(payment.getItemName())
                            .item_code(payment.getItemCode())
                            .created_at(payment.getCreatedAt())
                            .approved_at(payment.getApprovedAt())
                            .amount(payment.getAmount())
                            .payment_method_type(payment.getPaymentMethodType())
                            .build()
            );
        }

        return new ResponseEntity<>(paymentResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getMyReviews(KakaoMembers kakaoMember) {

        List<Reviews> reviewsList = reviewRepository.findAllByKakaoMember(kakaoMember);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (Reviews review : reviewsList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .review_id(review.getReviewId())
                            .nickname(review.getKakaoMember().getNickname())
                            .star(review.getStar())
                            .comment(review.getComment())
                            .build()
            );
        }

        return new ResponseEntity<>(reviewResponseDtoList, HttpStatus.OK);
    }
}
