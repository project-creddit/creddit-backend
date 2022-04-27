package com.creddit.credditmainserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Follower {

    @Id
    @GeneratedValue
    private Long id;
    private Long follwer; //memberId
    private Long following; //memberId

    public Follower(){

    }

    public Follower(Long follower, Long following){
        this.following = following;
        this.follwer = follower;
    }
}
