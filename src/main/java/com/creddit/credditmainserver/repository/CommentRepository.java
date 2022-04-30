package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByIdLessThanAndPostIdOrderByIdDesc(Long id, Long postId, Pageable pageable);

    @Query("select c from Comment c left join c.likes l left join c.post p " +
            "where c.id < :id and p.id = :postId " +
            "group by p.id, c.id, l.comment " +
            "order by count(l.comment) desc, c.id desc")
    Page<Comment> findByPageOfLikes(@Param("postId") Long postId, @Param("id") Long id, Pageable pageable);
}
