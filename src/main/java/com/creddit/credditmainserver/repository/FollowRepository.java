package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follower, Long> {
    List<Follower> findAllByFollower(Member follower);

    @Query("select f from Follower f join fetch f.following where f.follower = :follower")
    List<Follower> findAllFollowings(@Param("follower") Member follower);

    Optional<Follower> findByFollowingAndAndFollower(Member following, Member follower);

    void deleteAllById(Long id);
}
