package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {

    private Long id;
    private MemberResponseDto member;
    private String title;
    private String content;
    private Image image;
    private Long comments;
    private Long likes;
    private boolean isLiked;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likes = post.getLikes().stream().count();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();

        this.member = new MemberResponseDto(post.getMember());

        this.comments = post.getComments().stream().count();

        this.image = Image.builder()
                .imgName(post.getImgName())
                .imgUrl(post.getImgUrl())
                .build();

        this.isLiked = post.getLikes().stream().anyMatch(
                like -> like.getMember()
                        .getId()
                        .equals(SecurityUtil.getCurrentMemberId())
        ) ? true : false;
    }
}
