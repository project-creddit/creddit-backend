package com.creddit.credditmainserver.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Like(Member member, Post post, Comment comment){
        this.member = member;
        this.post = post;
        this.comment = comment;

        post.getLikes().add(this);
        comment.getLikes().add(this);
    }
}
