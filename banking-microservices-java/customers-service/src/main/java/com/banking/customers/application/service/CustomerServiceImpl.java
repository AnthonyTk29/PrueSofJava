package com.banking.customers.application.service;

import com.banking.customers.application.dto.CustomerCreateRequestDTO;
import com.banking.customers.application.dto.CustomerResponseDTO;
import com.banking.customers.application.dto.CustomerUpdateRequestDTO;
import com.banking.customers.application.event.CustomerEventPublisher;
import com.banking.customers.application.mapper.CustomerMapper;
import com.banking.customers.domain.entity.Customer;
import com.banking.customers.domain.exception.CustomerNotFoundException;
import com.banking.customers.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventPublisher eventPublisher;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                                CustomerEventPublisher eventPublisher,
                                CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAll() {
        return customerRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con Id " + id + " no encontrado"));
        return mapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO create(CustomerCreateRequestDTO request) {
        Customer customer = mapper.toEntity(request);
        customer.validate();
        Customer saved = customerRepository.save(customer);
        eventPublisher.publishCreated(mapper.toEventPayload(saved, "CustomerCreated"));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponseDTO update(Integer id, CustomerUpdateRequestDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con Id " + id + " no encontrado"));
        mapper.updateEntity(customer, request);
        customer.validate();
        Customer updated = customerRepository.save(customer);
        eventPublisher.publishUpdated(mapper.toEventPayload(updated, "CustomerUpdated"));
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponseDTO patchStatus(Integer id, boolean active) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con Id " + id + " no encontrado"));
        if (active) customer.enable();
        else customer.disable();
        Customer updated = customerRepository.save(customer);
        eventPublisher.publishUpdated(mapper.toEventPayload(updated, "CustomerUpdated"));
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponseDTO delete(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con Id " + id + " no encontrado"));
        CustomerResponseDTO response = mapper.toResponse(customer);
        eventPublisher.publishDeleted(mapper.toEventPayload(customer, "CustomerDeleted"));
        customerRepository.deleteById(id);
        return response;
    }
}
