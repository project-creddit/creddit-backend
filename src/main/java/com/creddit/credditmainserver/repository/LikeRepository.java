package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByPostIdAndMemberId(Long postId, Long memberId);

    Like findByCommentIdAndMemberId(Long postId, Long memberId);
}
