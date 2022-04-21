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
    private Long likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgName = post.getImgName();
        this.imgUrl = post.getImgUrl();
        this.likes = post.getLikes().stream().count();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();

        this.member = MemberResponseDto.builder()
                .email(post.getMember().getEmail())
                .nickname(post.getMember().getNickname())
                .build();

        this.comments = post.getComments().stream().map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
