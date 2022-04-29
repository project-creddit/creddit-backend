package com.creddit.credditmainserver.domain;

import com.creddit.credditmainserver.dto.response.FollowListResponseDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class Member{

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private boolean activated;

    private String introduction;

    private String imgUrl;

    private String imgName;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follower> followings = new ArrayList<>();

    public FollowListResponseDto memberToFollowList(){
        return new FollowListResponseDto(email,nickname,imgUrl, imgName);
    }

    public void setProfile(String imgUrl, String imgName, String introduction){
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.introduction = (introduction == null) ? "" : introduction;
    }
    public static Member toEntity(String email, String password, String nickname, Authority authority, boolean activated) {
        Member member= new Member();
        member.setEmail(email);
        member.setPassword(password);
        member.setNickname(nickname);
        member.setAuthority(authority);
        member.setActivated(activated);
        return member;
    }
}
