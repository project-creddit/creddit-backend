package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    @Query("select p from Post p where p.id < :id and (p.title like %:keyword% or p.content like %:keyword%) order by p.id desc")
    Page<Post> findByPageOfSearching(@Param("id") Long id,@Param("keyword") String keyword, Pageable pageable);
}
