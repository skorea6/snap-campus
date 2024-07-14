package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.entity.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByMemberAndPost(Member member, Post post);
    Page<PostLike> findAllByMember(Pageable pageable, Member member);
}
