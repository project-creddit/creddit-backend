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

    public LikeResponseDto(Like entity){
        this.likeId = entity.getId();
        this.postId = entity.getPost().getId();
        this.commentId = entity.getComment().getId();
        this.createdDate = entity.getCreatedDate();

        this.member = MemberResponseDto.builder()
                .email(entity.getMember().getEmail())
                .nickname(entity.getMember().getNickname())
                .build();
    }

}
