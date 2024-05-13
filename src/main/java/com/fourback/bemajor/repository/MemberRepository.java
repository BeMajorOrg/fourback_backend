package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String id);
}
