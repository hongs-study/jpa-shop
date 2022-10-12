package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.Collection;
import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findAllByName(String name);

    Member findByName(String name);

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

    @Query(
        value = "select m from Member m join m.team t where m.age = :age"
        , countQuery = "select count(m) from Member m where m.age = :age"
    )
    Page<Member> findByAge(@Param("age") int age, Pageable pageable);
    Slice<Member> findByName(String userName, Pageable pageable);
    List<Member> findByNickName(String nickName, Pageable pageable);

    @Modifying(clearAutomatically = true) // 이 어노테이션이 있어야 executeUpdate() 를 실행한다. clearAutomatically = true를 해야 영속성컨텍스트 문제가 없다
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    //@EntityGraph - 인터페이스 JpaRepository에서 제공하는 기본 메서드 - Override + EntityGraph 적용
    @EntityGraph(attributePaths = {"team"})
    @Override
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Override
    Page<Member> findAll(Pageable pageable);

    //@EntityGraph - JPQL 직접작성인 경우
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberEntityGraph();

    //@EntityGraph - 자동생성 컬럼명메서드인 경우
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByName(@Param("name") String userName);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    Member findReadOnlyByName(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByName(String userName);
}
