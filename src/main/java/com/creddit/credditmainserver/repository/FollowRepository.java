package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByFollowingAndAndFollwer(Long following, Long follower);
    void deleteAllById(Long id);
}
