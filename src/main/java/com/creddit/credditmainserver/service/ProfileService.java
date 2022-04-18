package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.request.ProfileRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.dto.response.ProfileResponseDto;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;

    public ProfileResponseDto saveProfile(Long id, ProfileRequestDto profileRequestDto){
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("프로필 로드 오류 memberId =" + id));
        if(profileRequestDto.getIntroduction()== null){
            member.setIntroduction("");
        }
        else{
            member.setIntroduction(profileRequestDto.getIntroduction());
        }

        if(profileRequestDto.getImgUrl() == null){
            member.setImgUrl("");
        }
        else{
            member.setImgUrl(profileRequestDto.getImgUrl());
        }
        memberRepository.save(member);

        return ProfileResponseDto.builder()
                .introduction(member.getIntroduction())
                .nickname(member.getNickname()).
                build();
    }
    public ProfileResponseDto getProfile(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("프로필 로드 오류 memberId =" + id));
        return ProfileResponseDto.builder()
                .imgUrl(member.getImgUrl())
                .introduction(member.getIntroduction())
                .nickname(member.getNickname()).
                build();
    }
}
