package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT m FROM Member m WHERE m.nickname = :nickname")
    Optional<Member> findByNickname(@Param("nickname") String nickname);
}
