package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Member;
import lombok.Getter;

@Getter
public class FollowListResponseDto {

    private String email;

    private String nickname;

    private String imgUrl;

    private String imgName;

    public FollowListResponseDto(){

    }
    public FollowListResponseDto(String email,String nickname,String imgUrl, String imgName){
        this.email = email;
        this.imgName = imgName;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }
}
