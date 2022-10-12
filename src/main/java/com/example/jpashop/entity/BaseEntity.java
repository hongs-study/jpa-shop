package com.example.jpashop.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class) //이벤트 수신하겠다는 표시(순수JPA의 prePersist 같은)
@Getter
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity {

    // 등록자,수정자 - 아래처럼만 쓴다고 되지 않는다. -> 스프링빈에 AudigtorAware 인스턴스를 등록해줘야 한다
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
