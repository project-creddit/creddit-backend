package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long postId;
    private Long parentCommentId;
    private MemberResponseDto member;
    private String content;
    private Long likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isLiked;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.parentCommentId = comment.getParentCommentId();
        this.content = comment.getContent();
        this.likes = comment.getLikes().stream().count();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();

        this.member = new MemberResponseDto(comment.getMember());

        this.isLiked = comment.getLikes().stream().anyMatch(
                like -> like.getMember()
                        .getEmail()
                        .equals(member.getEmail())
        ) ? true : false;
    }
}
