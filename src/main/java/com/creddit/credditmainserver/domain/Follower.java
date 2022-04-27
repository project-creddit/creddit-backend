package com.creddit.credditmainserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
public class Follower {

    @Id
    @GeneratedValue
    private Long id;

    private Long follwer; //memberId

    private Long following; //memberId

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public Follower(){

    }

    public Follower(Long follower, Long following){
        this.following = following;
        this.follwer = follower;
    }
}
