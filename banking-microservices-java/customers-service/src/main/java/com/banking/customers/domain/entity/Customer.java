package com.banking.customers.domain.entity;

import com.banking.customers.domain.exception.InvalidCustomerDataException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends Person {

    private Integer clientId;
    private String password;
    private boolean active = true;

    public void validate() {
        if (getName() == null || getName().isBlank())
            throw new InvalidCustomerDataException("El nombre es obligatorio");
        if (getIdentification() == null || getIdentification().isBlank())
            throw new InvalidCustomerDataException("La identificación es obligatoria");
        if (getAddress() == null || getAddress().isBlank())
            throw new InvalidCustomerDataException("La dirección es obligatoria");
        if (getPhone() == null || getPhone().isBlank())
            throw new InvalidCustomerDataException("El teléfono es obligatorio");
        if (getAge() < 0)
            throw new InvalidCustomerDataException("La edad no puede ser negativa");
        if (password == null || password.isBlank())
            throw new InvalidCustomerDataException("La contraseña es obligatoria");
    }

    public void disable() {
        this.active = false;
    }

    public void enable() {
        this.active = true;
    }
}
