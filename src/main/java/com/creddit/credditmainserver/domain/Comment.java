package com.creddit.credditmainserver.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Long parentCommentId;

    @Column(nullable = false, length = 2000)
    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Formula("(select count(1) from comment c where c.parent_comment_id = id)")
    private Long detailCommentCount;

    @Builder
    public Comment(Member member, Post post, String content, Long parentCommentId) {
        this.member = member;
        this.post = post;
        this.content = content;
        this.parentCommentId = parentCommentId;

        post.getComments().add(this);
    }

    public void updateComment(String content){
        this.content = content;
    }
}
