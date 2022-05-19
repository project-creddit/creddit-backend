package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private Long postId;

    @NotBlank(message = "내용은 빈 칸일 수 없습니다.")
    private String content;

    private Long parentCommentId;

    @Builder
    public CommentRequestDto(Long postId, String content, Long parentCommentId){
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public Comment toEntity(Member member, Post post){
        return Comment.builder()
                .member(member)
                .post(post)
                .content(content)
                .parentCommentId(parentCommentId)
                .build();
    }
    
}
