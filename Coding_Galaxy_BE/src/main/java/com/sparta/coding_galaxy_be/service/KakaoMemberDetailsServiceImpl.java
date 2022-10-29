package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.repository.KakaoMembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoMemberDetailsServiceImpl implements UserDetailsService {
    
    private final KakaoMembersRepository kakaoMembersRepository;
    
    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        KakaoMembers kakaoMember = kakaoMembersRepository.findByNickname(nickname).orElseThrow(
                () -> new UsernameNotFoundException(nickname + "을(를) 찾을 수 없습니다.")
        );

        return new KakaoMemberDetailsImpl(kakaoMember);
    }
}
