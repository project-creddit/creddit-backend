package com.creddit.credditmainserver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponseDto {
    private String nickname;
    private String introduction;
    private String imgUrl;
}
