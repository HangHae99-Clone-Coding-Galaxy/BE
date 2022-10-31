package com.sparta.coding_galaxy_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.coding_galaxy_be.dto.KakaoMemberInformationDto;
import com.sparta.coding_galaxy_be.dto.TokenDto;
import com.sparta.coding_galaxy_be.entity.Authority;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.RefreshToken;
import com.sparta.coding_galaxy_be.repository.KakaoMembersRepository;
import com.sparta.coding_galaxy_be.repository.RefreshTokenRepository;
import com.sparta.coding_galaxy_be.security.JwtAuthFilter;
import com.sparta.coding_galaxy_be.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoMemberService {

    private final KakaoMembersRepository kakaoMembersRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOauth kakaoOauth;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse httpServletResponse) throws JsonProcessingException {

        String kakaoAccessToken = kakaoOauth.getKakaoAccessToken(code);

        KakaoMemberInformationDto kakaoMemberInformationDto = kakaoOauth.getKakaoMemberInfo(kakaoAccessToken);

        Long kakaoMemberId = kakaoMemberInformationDto.getKakaoMemberId();
        KakaoMembers kakaoMember = kakaoMembersRepository.findByKakaoMemberId(kakaoMemberId).orElse(null);

        if (kakaoMember == null){
            kakaoMember = KakaoMembers.builder()
                    .kakaoMemberId(kakaoMemberInformationDto.getKakaoMemberId())
                    .email(kakaoMemberInformationDto.getEmail())
                    .name("KAKAO" + UUID.randomUUID())
                    .profileImage(kakaoMemberInformationDto.getProfile_image_url())
                    .nickname(kakaoMemberInformationDto.getNickname())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .authority(Authority.ROLE_MEMBER)
                    .build();

            kakaoMembersRepository.save(kakaoMember);
        }

        UserDetails kakaoUserDetails = new KakaoMemberDetailsImpl(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoUserDetails, null, kakaoUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        httpServletResponse.setHeader(JwtAuthFilter.AUTHORIZATION_HEADER, JwtAuthFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        httpServletResponse.setHeader("RefreshToken", tokenDto.getRefreshToken());
        httpServletResponse.setHeader("AccessTokenExpireTime", tokenDto.getAccessTokenExpiresIn().toString());
        httpServletResponse.setHeader("Nickname", kakaoMember.getNickname());
        httpServletResponse.setHeader("Authority", kakaoMember.getAuthority().toString());

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }
}
