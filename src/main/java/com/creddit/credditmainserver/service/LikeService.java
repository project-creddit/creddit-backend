package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Like;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.LikeRequestDto;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import com.creddit.credditmainserver.repository.CommentRepository;
import com.creddit.credditmainserver.repository.LikeRepository;
import com.creddit.credditmainserver.repository.MemberRepository;
import com.creddit.credditmainserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long createPostLike(LikeRequestDto likeRequestDto){
        Member member = memberRepository.getById(SecurityUtil.getCurrentMemberId());
        Post post = postRepository.getById(likeRequestDto.getPostId());

        return likeRepository.save(likeRequestDto.postLikeToEntity(member, post)).getId();
    }

    public Long createCommentLike(LikeRequestDto likeRequestDto){
        Member member = memberRepository.getById(SecurityUtil.getCurrentMemberId());
        Comment comment = commentRepository.getById(likeRequestDto.getCommentId());

        return likeRepository.save(likeRequestDto.commentLikeToEntity(member, comment)).getId();
    }
    
    public void deleteLike(Long id){
        Like like = likeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 없습니다. id = " + id));

        likeRepository.delete(like);
    }
}
