package com.banking.customers.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Person {

    private String name;
    private String gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
