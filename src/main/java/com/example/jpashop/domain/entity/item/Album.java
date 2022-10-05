package com.example.jpashop.domain.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue(value = "A")
@Entity
public class Album extends Item {
    private String artist;
    private String etc;
}