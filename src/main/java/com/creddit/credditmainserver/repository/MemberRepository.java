package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    @Query("SELECT m FROM Member m WHERE m.nickname = :nickname")
    Optional<Member> findByNickname(@Param("nickname") String nickname);
    @Query("select m from Member m " +
            "where m.nickname like %:keyword% or m.email like %:keyword% " +
            "order by m.id desc")
    Page<Member> findBySearch(@Param("keyword") String keyword, Pageable pageable);
}
