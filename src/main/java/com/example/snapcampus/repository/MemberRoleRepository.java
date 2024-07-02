package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
}
