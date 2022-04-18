package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Like;
import com.creddit.credditmainserver.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {

    private Long id;
    private MemberResponseDto member;
    private String title;
    private String content;
    private String imgName;
    private String imgUrl;
    private List<CommentResponseDto> comments;
    private List<Like> likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imgName = entity.getImgName();
        this.imgUrl = entity.getImgUrl();
        this.likes = entity.getLikes();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();

        this.member = MemberResponseDto.builder()
                .email(entity.getMember().getEmail())
                .nickname(entity.getMember().getNickname())
                .build();

        this.comments = entity.getComments().stream().map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
