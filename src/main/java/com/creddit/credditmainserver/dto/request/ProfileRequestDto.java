package com.creddit.credditmainserver.dto.request;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class ProfileRequestDto {
    @Size(min=0,max=50)
    private String introduction;

    private String imgUrl;
}
