package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private MemberResponseDto member;
    private Image profile;
    private String title;
    private String content;
    private Image image;
    private Long comments;
    private Long likes;
    private boolean isLiked;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post post, Member currentMember) {
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

        this.profile = Image.builder()
                .imgName(post.getMember().getImgName())
                .imgUrl(post.getMember().getImgUrl())
                .build();

        this.isLiked = currentMember != null && post.getLikes().stream().anyMatch(
                like -> like.getMember()
                        .getId()
                        .equals(currentMember.getId())
        );
    }
}
