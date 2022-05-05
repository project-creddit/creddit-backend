package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long postId;
    private MemberResponseDto member;
    private Image profile;
    private String content;
    private Long likes;
    private boolean isLiked;
    private Long detailCommentCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.likes = comment.getLikes().stream().count();
        this.detailCommentCount = comment.getDetailCommentCount();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();

        this.member = new MemberResponseDto(comment.getMember());

        this.profile = Image.builder()
                .imgName(comment.getMember().getImgName())
                .imgUrl(comment.getMember().getImgUrl())
                .build();

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
