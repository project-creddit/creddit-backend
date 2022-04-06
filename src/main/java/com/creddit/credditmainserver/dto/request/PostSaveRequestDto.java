package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    @NotNull
    private Member member;

    @NotBlank(message = "제목은 빈 칸일 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 빈 칸일 수 없습니다.")
    private String content;

    private String imgName;

    @Builder
    public PostSaveRequestDto(Member member, String title, String content, String imgName) {
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
