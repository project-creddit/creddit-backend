package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {

    private Member member;
    private String title;
    private String content;
    private String imgName;

    @Builder
    public PostRequestDto(Member member, String title, String content, String imgName) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.imgName = imgName;
    }

    public Post toEntity(){
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .imgName(imgName)
                .build();
    }
}
