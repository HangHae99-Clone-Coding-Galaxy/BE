package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.entity.MemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {
    
    private final MembersRepository membersRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Members member = membersRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다.")
        );

        return new MemberDetailsImpl(member);
    }
}
