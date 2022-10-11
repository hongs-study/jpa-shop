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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    @DisplayName("쿼리메서드기능3-파라미터바인딩IN")
    @Test
    void findByName() {
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
        Member 회원2 = memberRepository.findByName("회원2");

        System.out.println("회원2 ==> " + 회원2);
    }

    @DisplayName("페이징")
    @Test
    void paging() {
        //given
        Team 팀1 = new Team("팀1");
        teamRepository.save(팀1);
        memberRepository.save(new Member("회원1", 10, 팀1));
        memberRepository.save(new Member("회원1", 20, 팀1));
        memberRepository.save(new Member("회원1", 20, 팀1));
        memberRepository.save(new Member("회원4", 20, 팀1));
        memberRepository.save(new Member("회원5", 20, 팀1));

        int age = 20;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Direction.DESC, "name"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // DTO 객체로 변환
        Page<MemberDto> mapMember = page.map(
            m -> new MemberDto(m.getId(), m.getName(), m.getTeam().getName()));

        mapMember.forEach(e -> System.out.println(e));
        System.out.println("===============");

        // 페이지 계산 공식... totalPage, 마지막 페이지, 최초 페이지... => 복잡하고 짜증남
        System.out.println("page.getTotalElements() => " + page.getTotalElements());
        System.out.println("totalPage => " + page.getTotalPages());
        System.out.println("currentPageNumber => " + page.getNumber());
        System.out.println("page.isFirst() => " + page.isFirst());
        System.out.println("page.isLast() => " + page.isLast());
        System.out.println("page.hasNext() => " + page.hasNext());

        // Slice 테스트 => totalCount 쿼리 없음
        //Slice<Member> slice = memberRepository.findByName("회원1", pageRequest);
        //System.out.println("slice currentPageNumber => " + slice.getNumber());
        //System.out.println("slice.isFirst() => " + slice.isFirst());
        //System.out.println("slice.isLast() => " + slice.isLast());
        //System.out.println("slice.hasNext() => " + slice.hasNext());

        //then
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(4);

        page.forEach(e -> System.out.println(e));
    }

    @DisplayName("[벌크수정쿼리]")
    @Test
    void bulkAgePlus() {
        //given
        memberRepository.save(new Member("회원1", 10, null));
        memberRepository.save(new Member("회원2", 20, null));
        memberRepository.save(new Member("회원3", 19, null));
        memberRepository.save(new Member("회원4", 25, null));
        memberRepository.save(new Member("회원5", 20, null));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        // 필수! 벌크연산 직후 반드시 쿼리를 모두 보내고, 영속성 컨텍스트를 비워주자. => 그 다음 로직에서 영속성컨텍스트를 리프레쉬된 데이터로 다시 채울 수 있게!
        //em.flush(); => JPA에서는 벌크연산이 있으면 벌크연산직전에 모두 flush를 호출먼저 한다.
        //em.clear(); => @Modifying(clearAutomatically = true) 를 하면 SpringJPA에서 자동으로 clear를 해준다

        List<Member> 회원5 = memberRepository.findByUserName("회원5");
        assertThat(회원5.get(0).getAge()).isEqualTo(21);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @DisplayName("엔티티그래프")
    @Test
    void entityGraph() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.clear();

        //when
        //List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findMemberFetchJoin();
        List<Member> members = memberRepository.findEntityGraphByName("member1");

        //System.out.println("==> " + em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(members));

        //then
        members.forEach(e -> {
            System.out.println(e);
            System.out.println(e.getTeam());
        });

    }
}
