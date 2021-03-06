package com.creddit.credditmainserver.repository;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    /*전체 글 조회*/
    Page<Post> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    @Query("select p from Post p left join p.likes l " +
            "group by p.id " +
            "order by count(l.post) desc, p.id desc")
    Page<Post> findByLikes(Pageable pageable);

    @Query("select p from Post p left join p.member m " +
            "where p.id < :id " +
            "and m.id in (select f.following from Follower f where f.follower = :member) " +
            "order by p.id desc" )
    Page<Post> findByFollowing(@Param("id") Long id, @Param("member") Member member, Pageable pageable);

    /*특정 유저 글 조회*/
    Page<Post> findByIdLessThanAndMemberIdOrderByIdDesc(Long id, Long memberId, Pageable pageable);

    @Query("select p from Post p left join p.likes l " +
            "where p.member = :member " +
            "group by p.id " +
            "order by count(l.post) desc, p.id desc")
    Page<Post> findByMemberIdAndLikes(@Param("member") Member member, Pageable pageable);

    /*검색*/
    @Query("select p from Post p left join p.likes l " +
            "where p.title like %:keyword% or p.content like %:keyword% " +
            "group by p.id " +
            "order by count(l.post) desc, p.id desc")
    Page<Post> findBySearchAndLikes(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p from Post p left join p.member m " +
            "where p.id < :id " +
            "and (p.title like %:keyword% or p.content like %:keyword%) " +
            "and m.id in (select f.following from Follower f where f.follower = :member) " +
            "order by p.id desc")
    Page<Post> findBySearchAndFollowing(@Param("id") Long id, @Param("member") Member member, @Param("keyword") String keyword, Pageable pageable);

    @Query("select p from Post p " +
            "where p.id < :id " +
            "and (p.title like %:keyword% or p.content like %:keyword%) " +
            "order by p.id desc")
    Page<Post> findBySearch(@Param("id") Long id,@Param("keyword") String keyword, Pageable pageable);


}
