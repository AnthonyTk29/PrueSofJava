package com.banking.clientes.infrastructure.seed;

import com.banking.clientes.domain.entity.Cliente;
import com.banking.clientes.infrastructure.persistence.ClienteJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(1)
public class ClienteSeeder implements ApplicationRunner {

    private final ClienteJpaRepository clienteJpaRepository;

    public ClienteSeeder(ClienteJpaRepository clienteJpaRepository) {
        this.clienteJpaRepository = clienteJpaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (clienteJpaRepository.count() == 0) {
            log.info("Seeding clientes data...");
            clienteJpaRepository.saveAll(createSeedClientes());
            log.info("Clientes seed data inserted.");
        }
    }

    private List<Cliente> createSeedClientes() {
        Cliente c1 = new Cliente();
        c1.setNombre("Jose Lema");
        c1.setGenero("Masculino");
        c1.setEdad(14);
        c1.setIdentificacion("1234567890");
        c1.setDireccion("Otavalo sn y Quito");
        c1.setTelefono("098254785");
        c1.setContrasena("1234");
        c1.setEstado(true);

        Cliente c2 = new Cliente();
        c2.setNombre("Marianela Montalvo");
        c2.setGenero("Femenino");
        c2.setEdad(33);
        c2.setIdentificacion("0987654321");
        c2.setDireccion("Amazonas y NNUU");
        c2.setTelefono("097548965");
        c2.setContrasena("5678");
        c2.setEstado(true);

        Cliente c3 = new Cliente();
        c3.setNombre("Juan Osorio");
        c3.setGenero("Masculino");
        c3.setEdad(23);
        c3.setIdentificacion("1111111111");
        c3.setDireccion("13 junio y Equinoccial");
        c3.setTelefono("098874587");
        c3.setContrasena("1245");
        c3.setEstado(true);

        return List.of(c1, c2, c3);
    }
}
