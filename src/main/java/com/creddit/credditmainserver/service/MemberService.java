package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.response.FollowListResponseDto;
import com.creddit.credditmainserver.dto.request.PasswordRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.repository.FollowRepository;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.creddit.credditmainserver.domain.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<FollowListResponseDto> followList(Long id){
        Member member = memberRepository.findById(id).orElseThrow();
        List<Follower> followers = followRepository.findAllByFollower(member);
        List<Member> list = new ArrayList<>();
        followers.forEach(o->
                list.add(memberRepository.findById(o.getFollowing()).orElseThrow()));

        return list.stream().map(Member::memberToFollowList).collect(Collectors.toList());
    }

    @Transactional
    public Long follw(String nickname, Principal principal) throws Exception{
        Member follower = memberRepository.findById(Long.parseLong(principal.getName())).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 memberId =" + Long.parseLong(principal.getName())));
        Member following = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 nickname =" + nickname));

        if(followRepository.findByFollowingAndAndFollower(following.getId(), follower).isPresent()){
            throw new IllegalArgumentException("이미 팔로우한 회원입니다.");
        }
        Follower follow = new Follower(follower, following.getId());

        return followRepository.save(follow).getId();
    }

    @Transactional
    public Long deleteFollow(String nickname, Principal principal){
        Member follower = memberRepository.findById(Long.parseLong(principal.getName())).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 memberId =" + Long.parseLong(principal.getName())));
        Member following = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 nickname =" + nickname));
        Follower follow = followRepository.findByFollowingAndAndFollower(following.getId(),follower).orElseThrow(()-> new IllegalArgumentException("불가능한 요청입니다."));
        followRepository.deleteAllById(follow.getId());
        return follow.getId();
    }


    @Transactional(readOnly = true)
    public boolean checkDuplicateByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateByNickname(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public MemberResponseDto changeNickname(String nickname,Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 memberId =" + id));
        member.setNickname(nickname);
        memberRepository.save(member);

       return new MemberResponseDto(member);
    }
    @Transactional
    public Long changePassword(PasswordRequestDto passwordRequestDto, Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 memberId =" + id));
        member.setPassword(passwordEncoder.encode(passwordRequestDto.getPassword()));
        return memberRepository.save(member).getId();
    }

    public List<MemberResponseDto> findUserBySearch(int size, String keyword){
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Member> members = memberRepository.findBySearch(keyword, pageRequest);

        return members.stream().map(MemberResponseDto::new).collect(Collectors.toList());
    }
}
