package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Authority;
import com.creddit.credditmainserver.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberResponseDto {

    private String email;

    private String nickname;

    public MemberResponseDto(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
