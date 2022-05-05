package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Getter
public class DetailCommentResponseDto {

    private Long commentId;
    private Long parentCommentId;
    private MemberResponseDto member;
    private String content;
    private Long likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isLiked;

    public DetailCommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.content = comment.getContent();
        this.likes = comment.getLikes().stream().count();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();

        this.member = new MemberResponseDto(comment.getMember());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal.equals("anonymousUser")){
            this.isLiked = false;
        }else{
            this.isLiked = comment.getLikes().stream().anyMatch(
                    like -> like.getMember()
                            .getId()
                            .equals(SecurityUtil.getCurrentMemberId())
            ) ? true : false;
        }
    }
}
