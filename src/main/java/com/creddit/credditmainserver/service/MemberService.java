package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.repository.FollowRepository;
import com.creddit.credditmainserver.repository.MemberCustomRepository;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.creddit.credditmainserver.domain.*;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberCustomRepository memberCustomRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;


    @Transactional
    public Follower follw(String nickname, Principal principal){
        Long follower = Long.parseLong(principal.getName());
        Member following = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 nickname =" + nickname));
        Follower follow = new Follower(follower, following.getId());
        return followRepository.save(follow);
    }

    @Transactional
    public Long deleteFollow(String nickname, Principal principal){
        Member following = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 nickname =" + nickname));
        Follower follow = followRepository.findByFollowingAndAndFollwer(following.getId(),Long.parseLong(principal.getName())).orElseThrow();
        followRepository.deleteAllById(follow.getId());
        return follow.getId();
    }


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
