package com.creddit.credditmainserver.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String imgName;

    private String imgUrl;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Post(Member member, String title, String content, String imgName, String imgUrl){
        this.member = member;
        this.title = title;
        this.content = content;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public void updatePost(String title, String content, String imgName, String imgUrl){
        this.title = title;
        this.content = content;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
