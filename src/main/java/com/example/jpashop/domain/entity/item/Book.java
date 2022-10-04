package com.example.jpashop.domain.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue(value = "B")
@Entity
public class Book extends Item {
    private String author;
    private String isbn;
}
