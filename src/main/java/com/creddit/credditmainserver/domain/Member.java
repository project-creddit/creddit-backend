package com.creddit.credditmainserver.domain;

import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private boolean activated;

    private String introduction;

    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

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
