package com.creddit.credditmainserver.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class MemberRequestDto {

    @NotEmpty(message = "이메일을 입력해야 합니다.")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호를 입력해야 합니다.")
    @Pattern(regexp ="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",message = "비밀번호는 영어대소문자, 숫자, 특수문자를 모두 사용한 8~20자의 비밀번호여야 합니다.")
    private String password;

    @NotEmpty(message = "닉네임을 입력해야 합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email,password);
    }
    public MemberRequestDto(String email,String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
