package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Like;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequestDto {

    private Long postId;

    private Long commentId;

    @Builder
    public LikeRequestDto(Long postId, Long commentId){
        this.postId = postId;
        this.commentId = commentId;
    }

    public Like toEntity(Member member, Post post, Comment comment){
        return Like.builder()
                .member(member)
                .post(post)
                .comment(comment)
                .build();
    }
}
