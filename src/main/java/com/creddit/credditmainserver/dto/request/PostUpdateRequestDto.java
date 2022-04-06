package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequestDto {

    private String title;
    private String content;
    private String imgName;

    @Builder
    public PostUpdateRequestDto(String title, String content, String imgName) {
        this.title = title;
        this.content = content;
        this.imgName = imgName;
    }

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .imgName(imgName)
                .build();
    }
}
