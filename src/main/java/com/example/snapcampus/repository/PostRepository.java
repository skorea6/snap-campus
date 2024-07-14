package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByMemberOrderByCreatedAtDesc(Member member);
    List<Post> findAllByTitleContainingOrderByCreatedAtDesc(String title);
}
