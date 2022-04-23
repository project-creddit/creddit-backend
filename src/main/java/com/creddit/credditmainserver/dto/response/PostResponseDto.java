package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Image;
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
    private Image image;
    private List<CommentResponseDto> comments;
    private Long likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isLiked;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likes = post.getLikes().stream().count();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();

        this.member = new MemberResponseDto(post.getMember());

        this.comments = post.getComments().stream().map(CommentResponseDto::new)
                .collect(Collectors.toList());

        this.image = Image.builder()
                .imgName(post.getImgName())
                .imgUrl(post.getImgUrl())
                .build();

        this.isLiked = post.getLikes().stream().anyMatch(
                like -> like.getMember()
                        .getEmail()
                        .equals(member.getEmail())
        ) ? true : false;
    }
}
