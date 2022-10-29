package com.sparta.coding_galaxy_be.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class KakaoMemberDetailsImpl implements UserDetails {

    private final KakaoMembers kakaoMembers;

    public KakaoMemberDetailsImpl(KakaoMembers kakaoMembers){
        this.kakaoMembers = kakaoMembers;
    }

    public KakaoMembers getKakaoMembers(){
        return kakaoMembers;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Authority authority = kakaoMembers.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.toString());

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return kakaoMembers.getPassword();
    }

    @Override
    public String getUsername() {
        return kakaoMembers.getNickname();
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
