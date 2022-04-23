package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "제목은 빈 칸일 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 빈 칸일 수 없습니다.")
    private String content;

    private String imgName;

    private String imgUrl;

    @Builder
    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addImage(Image image){
        this.imgName= image.getImgName();
        this.imgUrl = image.getImgUrl();
    }

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .imgName(imgName)
                .imgUrl(imgUrl)
                .build();
    }
}
