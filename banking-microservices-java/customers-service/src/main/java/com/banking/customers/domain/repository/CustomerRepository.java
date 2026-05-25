package com.banking.customers.domain.repository;

import com.banking.customers.domain.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> findById(Integer id);

    List<Customer> findAll();

    Customer save(Customer customer);

    void deleteById(Integer id);

    boolean existsByIdentification(String identification);
}
