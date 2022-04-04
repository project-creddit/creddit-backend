package com.creddit.credditmainserver.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull(message = "제목은 빈 칸일 수 없습니다.")
    @Size(max = 50)
    private String title;

    @NotNull(message = "내용은 빈 칸일 수 없습니다.")
    @Column(columnDefinition = "TEXT")
    private String content;

    private String imgName;

    @Column(columnDefinition = "bigint default 0")
    private Long likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Post(Member member, String title, String content, String imgName){
        this.member = member;
        this.title = title;
        this.content = content;
        this.imgName = imgName;

        member.getPosts().add(this);
    }

    public void updatePost(String title, String content, String imgName){
        this.title = title;
        this.content = content;
        this.imgName = imgName;
    }
}
