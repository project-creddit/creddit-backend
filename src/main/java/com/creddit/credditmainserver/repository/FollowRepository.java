package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByFollowingAndAndFollower(Long following, Member follower);
    void deleteAllById(Long id);
}
