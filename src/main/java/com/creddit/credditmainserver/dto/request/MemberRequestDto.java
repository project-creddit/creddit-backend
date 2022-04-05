package com.creddit.credditmainserver.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberRequestDto {

    @NotEmpty(message = "이메일을 입력해야 합니다.")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호를 입력해야 합니다.")
    private String password;

    @NotEmpty(message = "닉네임을 입력해야 합니다.")
    private String nickname;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email,password);
    }
}
