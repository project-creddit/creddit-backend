package com.creddit.credditmainserver.dto.response;

import com.creddit.credditmainserver.domain.Like;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikeResponseDto {

    private Long likeId;

    private MemberResponseDto member;

    private Long postId;

    private Long commentId;

    private LocalDateTime createdDate;

    public LikeResponseDto(Like like){
        this.likeId = like.getId();
        this.postId = like.getPost().getId();
        this.commentId = like.getComment().getId();
        this.createdDate = like.getCreatedDate();

        this.member = new MemberResponseDto(like.getMember());
    }

}
