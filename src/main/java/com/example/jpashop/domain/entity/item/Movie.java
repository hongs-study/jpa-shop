package com.example.jpashop.domain.entity.item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue(value = "M")
@Entity
public class Movie extends Item {
    private String director;
    private String actor;
}
