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
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        KakaoMembers kakaoMember = kakaoMembersRepository.findByName(name).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다.")
        );

        return new KakaoMemberDetailsImpl(kakaoMember);
    }
}
