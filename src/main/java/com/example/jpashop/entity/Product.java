package com.example.jpashop.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// JPA식별자 전략에서 @GeneratedValue를 쓰지 않을 때! == 즉, ID 개발자가 직접 생성할 때!!!
// => jpaRepository의 save() 구현체의 로직을 재정의 해줘야 한다. = 즉, Psersistable 인터페이스의 isNew()를 직접 구현한다.
// isNew()의 로직은 강사님의 Tip!!! => createDate를 이용한다
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Product implements Persistable<String> {

    @Id //@GeneratedValue
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    public Product(String id) {
        this.id = id;
    }
}
