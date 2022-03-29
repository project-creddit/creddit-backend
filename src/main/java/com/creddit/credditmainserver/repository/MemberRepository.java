package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public boolean checkDuplicateByNickname(String nickname){
        try{
            Member member=em.createQuery("select m from Member m where m.nickname = :nickname",Member.class)
                    .setParameter("nickname",nickname)
                    .getSingleResult();
        }catch(NoResultException nre){
            return false;
        }
        return true;
    }

    public boolean checkDuplicateByEmail(String email){
        try{
            Member member=em.createQuery("select m from Member m where m.email = :email",Member.class)
                    .setParameter("email",email)
                    .getSingleResult();
        }
        catch (NoResultException nre){
            return false;
        }
        return true;
    }
}
