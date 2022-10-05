package com.example.jpashop.web;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "이름은 필수값입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;

}
