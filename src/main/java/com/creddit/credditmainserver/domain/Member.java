package com.creddit.credditmainserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private char delYN;


}
