package com.creddit.credditmainserver.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequestDto {
    String email;
    String nickname;
    String type;


}
