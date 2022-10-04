package com.example.jpashop.domain;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
