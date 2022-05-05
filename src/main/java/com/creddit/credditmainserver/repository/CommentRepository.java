package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c left join c.post p " +
            "where c.id < :lastCommentId and p.id = :postId and c.parentCommentId is null " +
            "order by c.id desc")
    Page<Comment> findByRecent(@Param("lastCommentId")Long lastCommentId, @Param("postId") Long postId, Pageable pageable);

    Page<Comment> findByIdLessThanAndParentCommentIdOrderByIdDesc(Long lastCommentId, Long parentCommentId, Pageable pageable);

    @Query("select c from Comment c left join c.likes l left join c.post p " +
            "where p.id = :postId and c.parentCommentId is null " +
            "group by p.id, c.id, l.comment " +
            "order by count(l.comment) desc, c.id desc")
    Page<Comment> findByLikes(@Param("postId") Long postId, Pageable pageable);
}
