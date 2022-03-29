package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private Member member;
    private String title;
    private String content;
    private String imgName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.member = entity.getMember();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imgName = entity.getImgName();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
