package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.request.MemberRequest;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public boolean checkDuplicateByNickname(String nickname){
        return memberRepository.checkDuplicateByNickname(nickname);
    }

    @Transactional
    public boolean checkDuplicateByEmail(String email){
        return memberRepository.checkDuplicateByEmail(email);
    }

    @Transactional
    public Long join(MemberRequest memberRequest){
        Member member=new Member();
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        member.setPassword(encoder.encode(memberRequest.getPassword()));
        member.setEmail(memberRequest.getEmail());
        member.setNickname(memberRequest.getNickname());
        member.setDelYN('N');

        return memberRepository.save(member);
    }
}
