package com.sparta.coding_galaxy_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.coding_galaxy_be.dto.KakaoMemberInformationDto;
import com.sparta.coding_galaxy_be.dto.TokenDto;
import com.sparta.coding_galaxy_be.dto.requestDto.MemberRequestDto;
import com.sparta.coding_galaxy_be.entity.Authority;
import com.sparta.coding_galaxy_be.entity.MemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.RefreshToken;
import com.sparta.coding_galaxy_be.repository.MembersRepository;
import com.sparta.coding_galaxy_be.repository.RefreshTokenRepository;
import com.sparta.coding_galaxy_be.security.JwtAuthFilter;
import com.sparta.coding_galaxy_be.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MembersRepository membersRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOauth kakaoOauth;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public ResponseEntity<?> signup(MemberRequestDto memberRequestDto) {

        if (membersRepository.existsByEmail(memberRequestDto.getEmail())){
            throw new RuntimeException("중복된 이메일입니다.");
        }

        Members member = Members.builder()
                .email(memberRequestDto.getEmail())
                .nickname(memberRequestDto.getNickname())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .authority(Authority.ROLE_MEMBER)
                .build();

        membersRepository.save(member);

        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> login(MemberRequestDto memberRequestDto, HttpServletResponse httpServletResponse) {

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Members member = membersRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new RuntimeException("가입되지 않은 회원입니다.")
        );

        if(!passwordEncoder.matches(memberRequestDto.getPassword(), member.getPassword()))
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        httpServletResponse.setHeader(JwtAuthFilter.AUTHORIZATION_HEADER, JwtAuthFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        httpServletResponse.setHeader("RefreshToken", tokenDto.getRefreshToken());
        httpServletResponse.setHeader("AccessTokenExpireTime", tokenDto.getAccessTokenExpiresIn().toString());
        httpServletResponse.setHeader("Nickname", member.getNickname());
        httpServletResponse.setHeader("Authority", member.getAuthority().toString());

        return new ResponseEntity<>("로그인 완료", HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse httpServletResponse) throws JsonProcessingException {

        String kakaoAccessToken = kakaoOauth.getKakaoAccessToken(code);

        KakaoMemberInformationDto kakaoMemberInformationDto = kakaoOauth.getKakaoMemberInfo(kakaoAccessToken);

        Long kakaoMemberId = kakaoMemberInformationDto.getKakaoMemberId();
        Members kakaoMember = membersRepository.findByKakaoMemberId(kakaoMemberId).orElse(null);

        if (kakaoMember == null){
            kakaoMember = Members.builder()
                    .kakaoMemberId(kakaoMemberInformationDto.getKakaoMemberId())
                    .email(kakaoMemberInformationDto.getEmail())
                    .profileImage(kakaoMemberInformationDto.getProfile_image_url())
                    .nickname(kakaoMemberInformationDto.getNickname())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .authority(Authority.ROLE_MEMBER)
                    .build();

            membersRepository.save(kakaoMember);
        }

        UserDetails kakaoUserDetails = new MemberDetailsImpl(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoUserDetails, null, kakaoUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        System.out.println(tokenDto.getAccessToken());

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
