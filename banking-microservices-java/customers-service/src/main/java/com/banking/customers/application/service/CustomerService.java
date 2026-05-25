package com.banking.customers.application.service;

import com.banking.customers.application.dto.CustomerCreateRequestDTO;
import com.banking.customers.application.dto.CustomerResponseDTO;
import com.banking.customers.application.dto.CustomerUpdateRequestDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDTO> getAll();

    CustomerResponseDTO getById(Integer id);

    CustomerResponseDTO create(CustomerCreateRequestDTO request);

    CustomerResponseDTO update(Integer id, CustomerUpdateRequestDTO request);

    CustomerResponseDTO patchStatus(Integer id, boolean active);

    CustomerResponseDTO delete(Integer id);
}
