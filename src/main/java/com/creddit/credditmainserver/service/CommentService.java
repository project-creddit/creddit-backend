package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.CommentRequestDto;
import com.creddit.credditmainserver.dto.response.CommentResponseDto;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import com.creddit.credditmainserver.repository.CommentRepository;
import com.creddit.credditmainserver.repository.MemberRepository;
import com.creddit.credditmainserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> fetchCommentPagesBy(Long postId, Long lastCommentId, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Comment> comments;

        if(sort.equals("like")){
            comments = commentRepository.findByPageOfLikes(postId, lastCommentId, pageRequest);
        }else{
            comments = commentRepository.findByIdLessThanAndPostIdOrderByIdDesc(lastCommentId, postId, pageRequest);
        }

        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

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
