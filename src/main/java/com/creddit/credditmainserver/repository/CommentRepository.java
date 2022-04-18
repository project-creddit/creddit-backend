package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
