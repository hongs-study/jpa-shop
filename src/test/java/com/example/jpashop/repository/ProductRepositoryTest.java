package com.example.jpashop.repository;

import com.example.jpashop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @DisplayName("save")
    @Test
    void saveTest() {
        //given

        Product product = new Product( "key01");
        Product pro = productRepository.save(product);
        System.out.println("createdDate = " + pro.getCreatedDate());
    }

}