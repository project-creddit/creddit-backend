package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Like;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long postId;
    private Long parentCommentId;
    private MemberResponseDto member;
    private String content;
    private List<Like> likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public CommentResponseDto(Comment entity){
        this.commentId = entity.getId();
        this.postId = entity.getPost().getId();
        this.parentCommentId = entity.getParentCommentId();
        this.content = entity.getContent();
        this.likes = entity.getLikes();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();

        this.member = MemberResponseDto.builder()
                .email(entity.getMember().getEmail())
                .nickname(entity.getMember().getNickname())
                .build();
    }
}
