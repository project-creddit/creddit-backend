package com.creddit.credditmainserver.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberRequest {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String nickname;
}
