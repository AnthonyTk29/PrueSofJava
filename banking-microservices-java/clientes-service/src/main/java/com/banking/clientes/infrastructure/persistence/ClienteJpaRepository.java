package com.banking.clientes.infrastructure.persistence;

import com.banking.clientes.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteJpaRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByIdentificacion(String identificacion);
}
