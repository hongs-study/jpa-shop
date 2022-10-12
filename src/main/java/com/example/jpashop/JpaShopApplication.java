package com.example.jpashop;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 생략가능 : @EnableJpaRepositories(basePackages = "com.example.jpashop")
@EnableJpaAuditing
@SpringBootApplication
public class JpaShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaShopApplication.class, args);
    }

    // SpringJPA의 Auditing 기능을 사용 할 때 등록자,수정자 등록하기 위한 빈.
    @Bean
    public AuditorAware<String> auditorProvider() {
        // 수정자등록자 입력이벤트가 생길 때마다 아래 함수가 실행된다.
        // 세션이나 스프링시큐리티에서 꺼내씀..
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}