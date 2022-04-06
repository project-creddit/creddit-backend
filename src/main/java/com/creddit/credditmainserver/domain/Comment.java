package com.creddit.credditmainserver.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "내용은 빈 칸일 수 없습니다.")
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "bigint default 0")
    private Long likeCount;

    @OneToMany(mappedBy = "comment")
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Comment(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;

        post.getComments().add(this);
    }

    public void updateComment(String content){
        this.content = content;
    }
}
