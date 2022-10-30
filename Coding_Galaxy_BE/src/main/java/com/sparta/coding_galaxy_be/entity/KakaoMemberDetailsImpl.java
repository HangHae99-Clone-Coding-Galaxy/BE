package com.sparta.coding_galaxy_be.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class KakaoMemberDetailsImpl implements UserDetails {

    private final KakaoMembers kakaoMember;

    public KakaoMemberDetailsImpl(KakaoMembers kakaoMember){
        this.kakaoMember = kakaoMember;
    }

    public KakaoMembers getKakaoMember(){
        return kakaoMember;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Authority authority = kakaoMember.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.toString());

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return kakaoMember.getPassword();
    }

    @Override
    public String getUsername() {
        return kakaoMember.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
