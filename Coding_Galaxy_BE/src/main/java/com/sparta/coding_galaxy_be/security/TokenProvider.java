package com.sparta.coding_galaxy_be.security;

import com.sparta.coding_galaxy_be.dto.TokenDto;
import com.sparta.coding_galaxy_be.entity.MemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.repository.MembersRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    private final MembersRepository membersRepository;
    private final Key key;

    @Autowired
    public TokenProvider(@Value("${jwt.secret.key}") String secretKey, MembersRepository membersRepository){
        this.membersRepository = membersRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRATION_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .build();
    }

    public Authentication getAuthentication(String accessToken){

        Claims claims = parseClaims(accessToken);

        Assert.notNull(claims.get(AUTHORITIES_KEY), "?????? ????????? ?????? ???????????????.");

        String email = claims.getSubject();
        Members member = membersRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("????????? ?????? ??? ????????????.")
        );

        MemberDetailsImpl memberDetails = new MemberDetailsImpl(member);

        return new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
    }

    private Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("????????? JWT ???????????????.");
        } catch (ExpiredJwtException e) {
            log.info("????????? JWT ???????????????.");
        } catch (UnsupportedJwtException e) {
            log.info("???????????? ?????? JWT ???????????????.");
        } catch (IllegalArgumentException e) {
            log.info("JWT ????????? ?????????????????????.");
        }
        return false;
    }
}
