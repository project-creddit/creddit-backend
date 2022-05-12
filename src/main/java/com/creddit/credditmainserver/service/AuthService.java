package com.creddit.credditmainserver.service;

import antlr.Token;
import com.creddit.credditmainserver.domain.Authority;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.ProviderType;
import com.creddit.credditmainserver.domain.RefreshToken;
import com.creddit.credditmainserver.dto.request.MemberRequestDto;
import com.creddit.credditmainserver.dto.request.SocialLoginRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.login.jwt.TokenDto;
import com.creddit.credditmainserver.login.jwt.TokenProvider;
import com.creddit.credditmainserver.login.jwt.TokenRequestDto;
import com.creddit.credditmainserver.repository.MemberRepository;
import com.creddit.credditmainserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final String key = "credit-project";


    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = Member.toEntity(memberRequestDto.getEmail(), passwordEncoder.encode(memberRequestDto.getPassword()),
                        memberRequestDto.getNickname(), Authority.ROLE_USER,true, ProviderType.LOCAL);

        memberRepository.save(member);

        return new MemberResponseDto(member);
    }


    @Transactional
    public Member socialSignup(SocialLoginRequestDto socialLoginRequestDto)throws Exception{
        ProviderType type = ProviderType.NAVER;
        if(socialLoginRequestDto.getType().equals("kakao")){
            type = ProviderType.KAKAO;
        }
        else if(!socialLoginRequestDto.getType().equals("kakao")&&!socialLoginRequestDto.getType().equals("naver")){
            throw new IllegalArgumentException("존재하지 않는 provider type");
        }

        Member newMember = Member.toEntity(socialLoginRequestDto.getEmail(),
                passwordEncoder.encode(socialLoginRequestDto.getEmail()+key),
                socialLoginRequestDto.getNickname(), Authority.ROLE_USER,true, type);

        memberRepository.save(newMember);

        return newMember;
    }

    @Transactional
    public TokenDto socialLogin(SocialLoginRequestDto socialLoginRequestDto) throws Exception {
        Member member = memberRepository.findByEmail(socialLoginRequestDto.getEmail())
                .orElse(socialSignup(socialLoginRequestDto));

        MemberRequestDto memberRequestDto = new MemberRequestDto(member.getEmail(),member.getNickname(),socialLoginRequestDto.getEmail()+key);

        return login(memberRequestDto);
    }


    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.createAccessRefreshToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissueAccessToken(TokenRequestDto tokenRequestDto){
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }


        return tokenProvider.reissue(authentication,tokenRequestDto.getRefreshToken());

    }

    @Transactional
    public TokenDto reissueAccessRefreshToken(TokenRequestDto tokenRequestDto) {

        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.createAccessRefreshToken(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
}
