package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Authority;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.RefreshToken;
import com.creddit.credditmainserver.dto.request.MemberRequestDto;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = Member.toEntity(memberRequestDto.getEmail(), passwordEncoder.encode(memberRequestDto.getPassword()),
                        memberRequestDto.getNickname(), Authority.ROLE_USER,true);

        memberRepository.save(member);

        return MemberResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
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
