package com.banking.customers.infrastructure.seed;

import com.banking.customers.infrastructure.persistence.CustomerJpaRepository;
import com.banking.customers.infrastructure.persistence.jpa.CustomerJpaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(1)
public class CustomerSeeder implements ApplicationRunner {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerSeeder(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (customerJpaRepository.count() == 0) {
            log.info("Seeding customers data...");
            customerJpaRepository.saveAll(createSeedCustomers());
            log.info("Customers seed data inserted.");
        }
    }

    private List<CustomerJpaEntity> createSeedCustomers() {
        CustomerJpaEntity c1 = new CustomerJpaEntity();
        c1.setName("Jose Lema");
        c1.setGender("Male");
        c1.setAge(14);
        c1.setIdentification("1234567890");
        c1.setAddress("Otavalo sn y Quito");
        c1.setPhone("098254785");
        c1.setPassword("1234");
        c1.setActive(true);

        CustomerJpaEntity c2 = new CustomerJpaEntity();
        c2.setName("Marianela Montalvo");
        c2.setGender("Female");
        c2.setAge(33);
        c2.setIdentification("0987654321");
        c2.setAddress("Amazonas y NNUU");
        c2.setPhone("097548965");
        c2.setPassword("5678");
        c2.setActive(true);

        CustomerJpaEntity c3 = new CustomerJpaEntity();
        c3.setName("Juan Osorio");
        c3.setGender("Male");
        c3.setAge(23);
        c3.setIdentification("1111111111");
        c3.setAddress("13 junio y Equinoccial");
        c3.setPhone("098874587");
        c3.setPassword("1245");
        c3.setActive(true);

        return List.of(c1, c2, c3);
    }
}