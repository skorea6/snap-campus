package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Comment;
import com.example.snapcampus.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostAndIsDeleted(Pageable pageable, Post post, Boolean isDeleted);
    Optional<Comment> findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
