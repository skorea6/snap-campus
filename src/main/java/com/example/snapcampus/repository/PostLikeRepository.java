package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByMemberAndPost(Member member, Post post);
    List<PostLike> findAllByMember(Member member);
}
