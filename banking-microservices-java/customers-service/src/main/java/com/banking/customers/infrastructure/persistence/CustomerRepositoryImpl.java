package com.banking.customers.infrastructure.persistence;

import com.banking.customers.domain.entity.Customer;
import com.banking.customers.domain.repository.CustomerRepository;
import com.banking.customers.infrastructure.persistence.jpa.CustomerJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity entity = toJpaEntity(customer);
        CustomerJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdentification(String identification) {
        return jpaRepository.existsByIdentification(identification);
    }

    private Customer toDomain(CustomerJpaEntity entity) {
        Customer customer = new Customer();
        customer.setClientId(entity.getClientId());
        customer.setName(entity.getName());
        customer.setGender(entity.getGender());
        customer.setAge(entity.getAge());
        customer.setIdentification(entity.getIdentification());
        customer.setAddress(entity.getAddress());
        customer.setPhone(entity.getPhone());
        customer.setPassword(entity.getPassword());
        customer.setActive(entity.isActive());
        return customer;
    }

    private CustomerJpaEntity toJpaEntity(Customer customer) {
        CustomerJpaEntity entity = new CustomerJpaEntity();
        entity.setClientId(customer.getClientId());
        entity.setName(customer.getName());
        entity.setGender(customer.getGender());
        entity.setAge(customer.getAge());
        entity.setIdentification(customer.getIdentification());
        entity.setAddress(customer.getAddress());
        entity.setPhone(customer.getPhone());
        entity.setPassword(customer.getPassword());
        entity.setActive(customer.isActive());
        return entity;
    }
}