package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByName(String name);
}
