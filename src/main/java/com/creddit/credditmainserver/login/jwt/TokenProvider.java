package com.creddit.credditmainserver.login.jwt;

import com.fasterxml.jackson.databind.ser.std.ToEmptyObjectSerializer;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider{

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  //7일

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}")String secret){
        byte[] keyBytes= Decoders.BASE64.decode(secret);
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) //sub: name
                .claim(AUTHORITIES_KEY,authorities) //auth: role
                .signWith(key, SignatureAlgorithm.HS512) //alg: HS512
                .setExpiration(validity) //exp: 토큰 만료 시간
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))//만료날짜
                .signWith(key, SignatureAlgorithm.HS512)//복호 알고리즘 정보
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .validty(validity.getTime())
                .refreshToken(refreshToken)
                .build();
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal =new User(claims.getSubject(),"",authorities); //클레임에서 name을 빼서 authentication 리턴
        return new UsernamePasswordAuthenticationToken(principal,"",authorities);
    }

    public boolean validateToken(String token){
        try{//토큰 내용 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명");
        }
        catch(ExpiredJwtException e){
            logger.info("만료된 JWT 토큰");
        }
        catch(UnsupportedJwtException e){
            logger.info("지원되지 않는 JWT 토큰");
        }
        catch(IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다");
        }

        return false;
    }
}
