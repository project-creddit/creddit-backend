package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Authority;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @WithMockUser(username = "테스트")
    @Profile("닉네임으로 유저 검색")
    public void 유저를_닉네임으로_검색() throws Exception{
        // given
        String nickName1 = "테스트1";
        String email1 = "test@naver.com";
        String nickName2 = "테스트2";
        String email2 = "test@daum.com";

        createMember(email1, nickName1);
        createMember(email2, nickName2);
        PageRequest pageRequest = PageRequest.of(0, 2);

//        // when
        Page<Member> memberPages = memberService.findUserBySearch(pageRequest, "테스트");
        List<MemberResponseDto> members = memberPages.stream().map(MemberResponseDto::new).collect(Collectors.toList());
        //then
        MemberResponseDto member2 = members.get(0);
        assertThat(member2.getNickname()).isEqualTo(nickName2);
        assertThat(member2.getEmail()).isEqualTo(email2);

        MemberResponseDto member1 = members.get(1);
        assertThat(member1.getNickname()).isEqualTo(nickName1);
        assertThat(member1.getEmail()).isEqualTo(email1);
    }

    @Test
    @WithMockUser(username = "테스트")
    @Profile("이메일로 유저 검색")
    public void 유저를_이메일로_검색() throws Exception{
        // given
        String nickName1 = "테스트1";
        String email1 = "test@naver.com";
        String nickName2 = "테스트2";
        String email2 = "test@daum.com";

        createMember(email1, nickName1);
        createMember(email2, nickName2);
        PageRequest pageRequest = PageRequest.of(0, 2);

//        // when
        Page<Member> memberPages = memberService.findUserBySearch(pageRequest, "test");
        List<MemberResponseDto> members = memberPages.stream().map(MemberResponseDto::new).collect(Collectors.toList());

        //then
        MemberResponseDto member2 = members.get(0);
        assertThat(member2.getNickname()).isEqualTo(nickName2);
        assertThat(member2.getEmail()).isEqualTo(email2);

        MemberResponseDto member1 = members.get(1);
        assertThat(member1.getNickname()).isEqualTo(nickName1);
        assertThat(member1.getEmail()).isEqualTo(email1);
    }

    @Test
    @WithMockUser(username = "테스트")
    @Profile("닉네임으로 유저 검색이 되지 않는다")
    public void 유저를_닉네임으로_검색이_되지_않는다() throws Exception{
        // given
        String nickName1 = "테스트1";
        String email1 = "test@naver.com";
        String nickName2 = "테스트2";
        String email2 = "test@daum.com";

        createMember(email1, nickName1);
        createMember(email2, nickName2);
        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        Page<Member> members = memberService.findUserBySearch(pageRequest, "안녕");

        //then
        assertThat(members.getTotalPages()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "테스트")
    @Profile("이메일로 유저 검색이 되지 않는다")
    public void 유저를_이메일로_검색이_되지_않는다() throws Exception{
        // given
        String nickName1 = "테스트1";
        String email1 = "test@naver.com";
        String nickName2 = "테스트2";
        String email2 = "test@daum.com";

        createMember(email1, nickName1);
        createMember(email2, nickName2);
        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        Page<Member> memberPages = memberService.findUserBySearch(pageRequest, "oereo@gmail.com");
        List<MemberResponseDto> members = memberPages.stream().map(MemberResponseDto::new).collect(Collectors.toList());

        //then
        assertThat(members.size()).isEqualTo(0);
    }

    private Member createMember(String email, String nickName){
        Member member = Member.toEntity(email
                , "1234"
                , nickName
                , Authority.ROLE_USER
                , true);

        return memberRepository.save(member);
    }
}
