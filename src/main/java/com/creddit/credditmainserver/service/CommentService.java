package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.CommentRequestDto;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import com.creddit.credditmainserver.repository.CommentRepository;
import com.creddit.credditmainserver.repository.MemberRepository;
import com.creddit.credditmainserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long createComment(CommentRequestDto commentRequestDto){
        Member member = memberRepository.getById(SecurityUtil.getCurrentMemberId());
        Post post = postRepository.getById(commentRequestDto.getPostId());

        return commentRepository.save(commentRequestDto.toEntity(member, post)).getId();
    }

    public Long updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id = " + id));

        comment.updateComment(content);

        return id;
    }

    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id = " + id));

        commentRepository.delete(comment);
    }
}
