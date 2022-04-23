package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.repository.MemberCustomRepository;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberCustomRepository memberCustomRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean checkDuplicateByEmail(String email){
        return memberCustomRepository.checkDuplicateByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateByNickname(String nickname){
        return memberCustomRepository.checkDuplicateByNickname(nickname);
    }

    @Transactional
    public MemberResponseDto changeNickname(String nickname,Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 memberId =" + id));
        member.setNickname(nickname);
        memberRepository.save(member);

       return new MemberResponseDto(member);
    }
}
