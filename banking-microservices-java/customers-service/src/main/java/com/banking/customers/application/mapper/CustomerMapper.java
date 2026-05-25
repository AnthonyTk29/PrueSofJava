package com.banking.customers.application.mapper;

import com.banking.customers.application.dto.CustomerCreateRequestDTO;
import com.banking.customers.application.dto.CustomerResponseDTO;
import com.banking.customers.application.dto.CustomerUpdateRequestDTO;
import com.banking.customers.application.event.CustomerEventPayload;
import com.banking.customers.domain.entity.Customer;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CustomerMapper {

    public CustomerResponseDTO toResponse(Customer customer) {
        return new CustomerResponseDTO(
                customer.getClientId(),
                customer.getName(),
                customer.getGender(),
                customer.getAge(),
                customer.getIdentification(),
                customer.getAddress(),
                customer.getPhone(),
                customer.isActive()
        );
    }

    public Customer toEntity(CustomerCreateRequestDTO request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setGender(request.gender());
        customer.setAge(request.age());
        customer.setIdentification(request.identification());
        customer.setAddress(request.address());
        customer.setPhone(request.phone());
        customer.setPassword(request.password());
        customer.setActive(request.active());
        return customer;
    }

    public void updateEntity(Customer customer, CustomerUpdateRequestDTO request) {
        customer.setName(request.name());
        customer.setGender(request.gender());
        customer.setAge(request.age());
        customer.setIdentification(request.identification());
        customer.setAddress(request.address());
        customer.setPhone(request.phone());
        if (request.password() != null && !request.password().isBlank()) {
            customer.setPassword(request.password());
        }
        customer.setActive(request.active());
    }

    public CustomerEventPayload toEventPayload(Customer customer, String eventType) {
        return new CustomerEventPayload(
                eventType,
                Instant.now(),
                new CustomerEventPayload.CustomerData(
                        customer.getClientId(),
                        customer.getName(),
                        customer.getIdentification(),
                        customer.isActive()
                )
        );
    }
}
