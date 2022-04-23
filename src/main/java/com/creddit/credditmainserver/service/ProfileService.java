package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.ProfileRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
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

        Member member = findById(id);
        member.setProfile(profileRequestDto.getImage().getImgUrl(), profileRequestDto.getImage().getImgName(),profileRequestDto.getIntroduction());
        memberRepository.save(member);

        return new ProfileResponseDto(member);
    }

    public ProfileResponseDto getProfile(Long id){
        Member member = findById(id);
        return new ProfileResponseDto(member);
    }

    public Member findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("프로필 로드 오류 memberId =" + id));
        return member;
    }
}
