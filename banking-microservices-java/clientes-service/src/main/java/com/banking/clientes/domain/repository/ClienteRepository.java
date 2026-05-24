package com.banking.clientes.domain.repository;

import com.banking.clientes.domain.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Optional<Cliente> findById(Integer id);

    List<Cliente> findAll();

    Cliente save(Cliente cliente);

    void deleteById(Integer id);

    boolean existsByIdentificacion(String identificacion);
}
