package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
