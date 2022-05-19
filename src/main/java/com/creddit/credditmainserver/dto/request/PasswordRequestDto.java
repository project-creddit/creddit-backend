package com.creddit.credditmainserver.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Getter
public class PasswordRequestDto {

    @NotEmpty(message = "비밀번호를 입력해야 합니다.")
    @Pattern(regexp ="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",message = "비밀번호는 영어대소문자, 숫자, 특수문자를 모두 사용한 8~20자의 비밀번호여야 합니다.")
    private String password;


}
