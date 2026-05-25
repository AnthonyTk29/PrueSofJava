package com.banking.accounts.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerReadModel {

    private Integer id;
    private Integer clientId;
    private String name;
    private String identification;
    private boolean active = true;
}
