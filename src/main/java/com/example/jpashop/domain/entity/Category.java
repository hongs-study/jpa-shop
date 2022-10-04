package com.example.jpashop.domain.entity;

import com.example.jpashop.domain.entity.item.Item;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(  // 다대다 필수 어노테이션. 중간 테이블, 복합키 설정
        name = "tb_category_item"
        , joinColumns = @JoinColumn(name = "category_id")    // 이 엔티티의 키가 들어가는 외래키
        , inverseJoinColumns = @JoinColumn(name = "item_id") // 상대방 테이블의 외래키
    )
    private List<Item> items = new ArrayList<>();

    //////////////////////// 셀프 계층구조 ////////////////////
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    //////////////////////// 셀프 계층구조 ////////////////////
}
