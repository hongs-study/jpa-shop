package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByName(String name);

    // 쿼리메서드기능1 - SpringJPA: 컬럼명으로 쉽게 작성 가능
    List<Member> findByNameAndAgeGreaterThan(String userName, Integer userAge);

    List<Member> findTop3HelloBy();

    // 쿼리메서드기능2 - JpaNameQuery => SpringJPA: 메서드명이 일치하면 바로 사용 가능
    @Query(name = "Member.findByUserName") // 관례
    List<Member> findByUserName(@Param("userName") String userName);

}
