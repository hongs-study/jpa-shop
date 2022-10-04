package com.example.jpashop.domain.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue(value = "A")
@Entity
public class Album extends Item {
    private String artist;
    private String etc;
}