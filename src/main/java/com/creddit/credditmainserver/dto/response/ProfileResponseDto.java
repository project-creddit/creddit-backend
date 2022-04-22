package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.Collectors;

@Getter
@Setter
public class ProfileResponseDto {
    private String nickname;

    private String introduction;

    private Image image;

    public ProfileResponseDto(Member member) {

        this.nickname = member.getNickname();
        this.introduction = member.getIntroduction();
        this.image = Image.builder()
                .imgName(member.getImgName())
                .imgUrl(member.getImgUrl())
                .build();
    }
}
