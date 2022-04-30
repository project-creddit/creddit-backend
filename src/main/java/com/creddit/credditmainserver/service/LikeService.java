package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Comment;
import com.creddit.credditmainserver.domain.Like;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
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

    public void clickLike(Long id, String type){
        Member member = memberRepository.getById(SecurityUtil.getCurrentMemberId());
        boolean isLiked = isLiked(id, type, member.getId());

        if(isLiked){
            Like like = checkTypeAndGetLike(id, type, member.getId());
            likeRepository.delete(like);
        }else{
            checkTypeAndSaveLike(id, type, member);
        }
    }

    private boolean isLiked(Long id, String type, Long memberId) {
        Like like = checkTypeAndGetLike(id, type, memberId);

        return like != null;
    }

    private Like checkTypeAndGetLike(Long id, String type, Long memberId) {
        if(type.equals("post")){
            return likeRepository.findByPostIdAndMemberId(id, memberId);
        }else{
            return likeRepository.findByCommentIdAndMemberId(id, memberId);
        }
    }

    private void checkTypeAndSaveLike(Long id, String type, Member member) {
        Like like = new Like();

        if(type.equals("post")){
            Post post = postRepository.getById(id);
            likeRepository.save(like.createPostLike(member, post));
        }else{
            Comment comment = commentRepository.getById(id);
            likeRepository.save(like.createCommentLike(member, comment));
        }
    }
}
