package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.EditMyInfoRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.MypageResponseDto;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.repository.KakaoMembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final KakaoMembersRepository kakaoMembersRepository;

    public ResponseEntity<?> getMypage(KakaoMembers kakaoMember) {

        MypageResponseDto mypageResponseDto = MypageResponseDto.builder()
                .profileImage(kakaoMember.getProfileImage())
                .nickname(kakaoMember.getNickname())
                .build();

        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> editMyInfo(EditMyInfoRequestDto editMyInfoRequestDto, KakaoMembers kakaoMember) throws IOException {

        //S3 Service 적용 후 수정 필요
        kakaoMember.editMyInfo(editMyInfoRequestDto);
        kakaoMembersRepository.save(kakaoMember);

        return new ResponseEntity<>("내 정보 변경 성공", HttpStatus.OK);
    }

//    public ResponseEntity<?> getMyPayment(KakaoMembers kakaoMember) {
//    }
//
//    public ResponseEntity<?> getMyReviews(KakaoMembers kakaoMember) {
//    }
}
