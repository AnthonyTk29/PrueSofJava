package com.banking.clientes.application.service;

import com.banking.clientes.application.dto.ClienteCreateRequest;
import com.banking.clientes.application.dto.ClienteResponse;
import com.banking.clientes.application.dto.ClienteUpdateRequest;
import com.banking.clientes.application.event.ClienteEventPublisher;
import com.banking.clientes.application.mapper.ClienteMapper;
import com.banking.clientes.domain.entity.Cliente;
import com.banking.clientes.domain.exception.ResourceNotFoundException;
import com.banking.clientes.domain.repository.ClienteRepository;
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
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteEventPublisher eventPublisher;

    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteServiceImpl(clienteRepository, eventPublisher, new ClienteMapper());
    }

    @Test
    void create_success() {
        ClienteCreateRequest request = new ClienteCreateRequest(
                "Jose Lema", "Masculino", 14, "1234567890",
                "Otavalo sn y Quito", "098254785", "1234", true
        );
        Cliente saved = buildCliente(1, "Jose Lema", "1234567890", true);
        when(clienteRepository.save(any())).thenReturn(saved);
        doNothing().when(eventPublisher).publishCreated(any());

        ClienteResponse response = clienteService.create(request);

        assertThat(response.nombre()).isEqualTo("Jose Lema");
        assertThat(response.clienteId()).isEqualTo(1);
        verify(clienteRepository).save(any());
        verify(eventPublisher).publishCreated(any());
    }

    @Test
    void getById_notFound_throwsException() {
        when(clienteRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.getById(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void getAll_returnsAllClientes() {
        List<Cliente> clientes = List.of(
                buildCliente(1, "Jose Lema", "1234567890", true),
                buildCliente(2, "Marianela Montalvo", "0987654321", true)
        );
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<ClienteResponse> result = clienteService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).nombre()).isEqualTo("Jose Lema");
        assertThat(result.get(1).nombre()).isEqualTo("Marianela Montalvo");
    }

    @Test
    void update_success() {
        Cliente existing = buildCliente(1, "Jose Lema", "1234567890", true);
        existing.setContrasena("1234");
        ClienteUpdateRequest request = new ClienteUpdateRequest(
                "Jose Lema Updated", "Masculino", 15, "1234567890",
                "Nueva Direccion", "099999999", "nuevapass", true
        );
        when(clienteRepository.findById(1)).thenReturn(Optional.of(existing));
        when(clienteRepository.save(any())).thenReturn(existing);
        doNothing().when(eventPublisher).publishUpdated(any());

        ClienteResponse response = clienteService.update(1, request);

        assertThat(response).isNotNull();
        verify(eventPublisher).publishUpdated(any());
    }

    @Test
    void patchEstado_disables_cliente() {
        Cliente existing = buildCliente(1, "Jose Lema", "1234567890", true);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(existing));
        when(clienteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishUpdated(any());

        ClienteResponse response = clienteService.patchEstado(1, false);

        assertThat(response.estado()).isFalse();
    }

    @Test
    void patchEstado_enables_cliente() {
        Cliente existing = buildCliente(1, "Jose Lema", "1234567890", false);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(existing));
        when(clienteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishUpdated(any());

        ClienteResponse response = clienteService.patchEstado(1, true);

        assertThat(response.estado()).isTrue();
    }

    @Test
    void delete_success() {
        Cliente existing = buildCliente(1, "Jose Lema", "1234567890", true);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(existing));
        doNothing().when(clienteRepository).deleteById(1);
        doNothing().when(eventPublisher).publishDeleted(any());

        ClienteResponse response = clienteService.delete(1);

        assertThat(response.clienteId()).isEqualTo(1);
        verify(clienteRepository).deleteById(1);
        verify(eventPublisher).publishDeleted(any());
    }

    @Test
    void create_validatesNombreRequerido() {
        ClienteCreateRequest request = new ClienteCreateRequest(
                null, "Masculino", 14, "1234567890",
                "Otavalo", "098254785", "1234", true
        );

        assertThatThrownBy(() -> clienteService.create(request))
                .isInstanceOf(com.banking.clientes.domain.exception.DomainException.class)
                .hasMessageContaining("nombre");
    }

    private Cliente buildCliente(Integer id, String nombre, String identificacion, boolean estado) {
        Cliente c = new Cliente();
        try {
            var field = Cliente.class.getDeclaredField("clienteId");
            field.setAccessible(true);
            field.set(c, id);
        } catch (Exception ignored) {}
        c.setNombre(nombre);
        c.setGenero("Masculino");
        c.setEdad(20);
        c.setIdentificacion(identificacion);
        c.setDireccion("Direccion");
        c.setTelefono("099999999");
        c.setContrasena("pass");
        c.setEstado(estado);
        return c;
    }
}
