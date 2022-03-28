package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public boolean checkDuplicate(Member member){
       if(em.find(Member.class,member.getId())==null){
           return false;
       }
       else{
           return true;
       }
    }
}
