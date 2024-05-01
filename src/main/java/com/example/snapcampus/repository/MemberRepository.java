package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUserId(String userId);
    Member findByNick(String nick);
    Member findByEmail(String email);

}
