package com.banking.customers.application.service;

import com.banking.customers.application.dto.CustomerCreateRequestDTO;
import com.banking.customers.application.dto.CustomerResponseDTO;
import com.banking.customers.application.dto.CustomerUpdateRequestDTO;
import com.banking.customers.application.event.CustomerEventPublisher;
import com.banking.customers.application.mapper.CustomerMapper;
import com.banking.customers.domain.entity.Customer;
import com.banking.customers.domain.exception.CustomerNotFoundException;
import com.banking.customers.domain.exception.InvalidCustomerDataException;
import com.banking.customers.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEventPublisher eventPublisher;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository, eventPublisher, new CustomerMapper());
    }

    @Test
    void create_success() {
        CustomerCreateRequestDTO request = new CustomerCreateRequestDTO(
                "Jose Lema", "Male", 14, "1234567890",
                "Otavalo sn y Quito", "098254785", "1234", true
        );
        Customer saved = buildCustomer(1, "Jose Lema", "1234567890", true);
        when(customerRepository.save(any())).thenReturn(saved);
        doNothing().when(eventPublisher).publishCreated(any());

        CustomerResponseDTO response = customerService.create(request);

        assertThat(response.name()).isEqualTo("Jose Lema");
        assertThat(response.clientId()).isEqualTo(1);
        verify(customerRepository).save(any());
        verify(eventPublisher).publishCreated(any());
    }

    @Test
    void getById_notFound_throwsException() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getById(999))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void getAll_returnsAllCustomers() {
        List<Customer> customers = List.of(
                buildCustomer(1, "Jose Lema", "1234567890", true),
                buildCustomer(2, "Marianela Montalvo", "0987654321", true)
        );
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponseDTO> result = customerService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Jose Lema");
        assertThat(result.get(1).name()).isEqualTo("Marianela Montalvo");
    }

    @Test
    void update_success() {
        Customer existing = buildCustomer(1, "Jose Lema", "1234567890", true);
        existing.setPassword("1234");
        CustomerUpdateRequestDTO request = new CustomerUpdateRequestDTO(
                "Jose Lema Updated", "Male", 15, "1234567890",
                "New Address", "099999999", "newpass", true
        );
        when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenReturn(existing);
        doNothing().when(eventPublisher).publishUpdated(any());

        CustomerResponseDTO response = customerService.update(1, request);

        assertThat(response).isNotNull();
        verify(eventPublisher).publishUpdated(any());
    }

    @Test
    void patchStatus_disables_customer() {
        Customer existing = buildCustomer(1, "Jose Lema", "1234567890", true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishUpdated(any());

        CustomerResponseDTO response = customerService.patchStatus(1, false);

        assertThat(response.active()).isFalse();
    }

    @Test
    void patchStatus_enables_customer() {
        Customer existing = buildCustomer(1, "Jose Lema", "1234567890", false);
        when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishUpdated(any());

        CustomerResponseDTO response = customerService.patchStatus(1, true);

        assertThat(response.active()).isTrue();
    }

    @Test
    void delete_success() {
        Customer existing = buildCustomer(1, "Jose Lema", "1234567890", true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
        doNothing().when(customerRepository).deleteById(1);
        doNothing().when(eventPublisher).publishDeleted(any());

        CustomerResponseDTO response = customerService.delete(1);

        assertThat(response.clientId()).isEqualTo(1);
        verify(customerRepository).deleteById(1);
        verify(eventPublisher).publishDeleted(any());
    }

    @Test
    void create_validates_name_required() {
        CustomerCreateRequestDTO request = new CustomerCreateRequestDTO(
                null, "Male", 14, "1234567890",
                "Otavalo", "098254785", "1234", true
        );

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessageContaining("El nombre es obligatorio");
    }

    private Customer buildCustomer(Integer id, String name, String identification, boolean active) {
        Customer c = new Customer();
        try {
            var field = Customer.class.getDeclaredField("clientId");
            field.setAccessible(true);
            field.set(c, id);
        } catch (Exception ignored) {}
        c.setName(name);
        c.setGender("Male");
        c.setAge(20);
        c.setIdentification(identification);
        c.setAddress("Some Address");
        c.setPhone("099999999");
        c.setPassword("pass");
        c.setActive(active);
        return c;
    }
}
