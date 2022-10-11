package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.Collection;
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

    // 쿼리메서드기능3 - 리파지토리 메서드에 직접 쿼리 작성(실무에서 2번보다 많이 씀)
    @Query("select m from Member m where m.name = :userName and m.age = :userAge")
    List<Member> findUser(@Param("userName") String userName, @Param("userAge") int userAge);


    // 쿼리메서드기능3 - 컬럼 1개만 조회하기 (컬럼에 맞는 데이터타입으로 반환해주면 됨)
    @Query("select m.name from Member m")
    List<String> findUserNameList();

    // 쿼리메서드기능3 - DTO로 조회하기
    @Query("select new com.example.jpashop.repository.MemberDto(m.id, m.name, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.name in :userNames")
    List<Member> findByUserNameIn(@Param("userNames") Collection<String> userNames);
}
