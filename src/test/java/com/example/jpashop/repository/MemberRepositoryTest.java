package com.example.jpashop.repository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.jpashop.entity.Member;
import com.example.jpashop.entity.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @PersistenceContext
    private EntityManager em;

    @Rollback(value = false)
    @Transactional
    @DisplayName("회원저장과 조회 테스트")
    @Test
    void memberTest() {

        System.out.println("memberRepository = " + memberRepository.getClass());

        //given
        Member member = new Member("홍길동1");

        //when
        Member savedMember = memberRepository.save(member);
        em.clear();
        Member findMember = memberRepository.findById(savedMember.getId())
            .orElseThrow(() -> new IllegalStateException("멤버가 조회되지 않습니다."));

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
    }

    @DisplayName("멤버 CRUD")
    @Test
    void crudTest() {
        //given
        Member member1 = new Member("회원1");
        Member member2 = new Member("회원2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건조회
        Member findMem1 = memberRepository.findById(member1.getId()).get();
        Member findMem2 = memberRepository.findById(member2.getId()).get();
        assertThat(member1).isEqualTo(findMem1);
        assertThat(member2).isEqualTo(findMem2);

        findMem1.setName("변경!!");

        // 다건조회
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 건수조회
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long afterDelCount = memberRepository.count();
        assertThat(afterDelCount).isEqualTo(0);

    }

    @DisplayName("쿼리메서드기능1-컬럼명으로메서드생성")
    @Test
    void testQuery() {
        //given
        Member member1 = new Member("회원1", 10, null);
        Member member2 = new Member("회원1", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findTop3HelloBy();

        //then
        assertThat(members.size()).isGreaterThan(1);

    }

    @DisplayName("쿼리메서드기능2-JpaNamedQuery")
    @Test
    void jpaNamedQuery() {
        //given
        Member member1 = new Member("회원1", 10, null);
        Member member2 = new Member("회원1", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findByUserName("회원1");

        //then
        assertThat(members.get(0).getName()).isEqualTo("회원1");
    }

    @DisplayName("쿼리메서드기능3-@Query에 직접 JPQL작성")
    @Test
    void jpaQueryMethod() {
        //given
        Member member1 = new Member("회원1", 10, null);
        Member member2 = new Member("회원1", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findUser("회원1", 10);

        //then
        assertThat(members.get(0).getName()).isEqualTo("회원1");
        assertThat(members.get(0).getAge()).isEqualTo(10);
    }

    @DisplayName("쿼리메서드기능3-단순히값하나조회")
    @Test
    void test123() {
        //given
        Member member1 = new Member("회원1", 10, null);
        Member member2 = new Member("회원1", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<String> userNameList = memberRepository.findUserNameList();
        userNameList.forEach(e -> System.out.println(e));
    }

    @DisplayName("쿼리메서드기능3-단순히값하나조회")
    @Test
    void test1234() {
        //given
        Team 팀1 = new Team("팀1");
        teamRepository.save(팀1);
        Member member1 = new Member("회원1", 10, 팀1);
        Member member2 = new Member("회원1", 20, 팀1);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<MemberDto> memberDto = memberRepository.findMemberDto();
        memberDto.forEach(e -> System.out.println(e));
    }

    @DisplayName("쿼리메서드기능3-파라미터바인딩IN")
    @Test
    void findByUserNameIn() {
        //given
        Team 팀1 = new Team("팀1");
        teamRepository.save(팀1);
        Member member1 = new Member("회원1", 10, 팀1);
        Member member2 = new Member("회원2", 20, 팀1);
        Member member3 = new Member("회원3", 20, 팀1);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        List<Member> members = memberRepository.findByUserNameIn(List.of("회원1", "회원2"));

        //then
        assertThat(members.size()).isEqualTo(2);
        members.forEach(e -> System.out.println(e));
    }


}
