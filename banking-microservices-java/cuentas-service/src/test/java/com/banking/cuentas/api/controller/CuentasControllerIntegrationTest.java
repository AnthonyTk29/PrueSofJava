package com.banking.cuentas.api.controller;

import com.banking.cuentas.domain.entity.ClienteReadModel;
import com.banking.cuentas.infrastructure.messaging.RabbitMqClienteEventConsumer;
import com.banking.cuentas.infrastructure.persistence.ClienteReadModelJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CuentasControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteReadModelJpaRepository clienteReadModelJpaRepository;

    // Necesario para que el contexto arranque sin RabbitMQ real
    @MockBean
    private ConnectionFactory connectionFactory;

    // Evita que @RabbitListener intente conectar al broker durante tests
    @MockBean
    private RabbitMqClienteEventConsumer rabbitMqClienteEventConsumer;

    @BeforeEach
    void setUp() {
        if (!clienteReadModelJpaRepository.existsByClienteId(10)) {
            ClienteReadModel cliente = new ClienteReadModel();
            cliente.setClienteId(10);
            cliente.setNombre("Test Cliente");
            cliente.setIdentificacion("5555555555");
            cliente.setEstado(true);
            clienteReadModelJpaRepository.save(cliente);
        }
    }

    @Test
    void createCuenta_returns201() throws Exception {
        Map<String, Object> body = Map.of(
                "numeroCuenta", "TEST001",
                "tipoCuenta", "AHORRO",
                "saldoInicial", new BigDecimal("1000.00"),
                "clienteId", 10,
                "estado", true
        );

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.numeroCuenta").value("TEST001"))
                .andExpect(jsonPath("$.data.tipoCuenta").value("AHORRO"))
                .andExpect(jsonPath("$.data.saldoInicial").value(1000.00));
    }

    @Test
    void createCuenta_clienteInexistente_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "numeroCuenta", "NOEXISTE",
                "tipoCuenta", "AHORRO",
                "saldoInicial", new BigDecimal("500.00"),
                "clienteId", 9999,
                "estado", true
        );

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createCuenta_tipoCuentaInvalido_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "numeroCuenta", "TIPO_MAL",
                "tipoCuenta", "INVALIDO",
                "saldoInicial", new BigDecimal("500.00"),
                "clienteId", 10,
                "estado", true
        );

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/cuentas/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createMovimiento_deposito_returns201() throws Exception {
        // Crear cuenta
        Map<String, Object> cuentaBody = Map.of(
                "numeroCuenta", "MOV001",
                "tipoCuenta", "AHORRO",
                "saldoInicial", new BigDecimal("500.00"),
                "clienteId", 10,
                "estado", true
        );
        mockMvc.perform(post("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaBody)));

        // Crear depósito (valor positivo)
        Map<String, Object> movBody = Map.of(
                "numeroCuenta", "MOV001",
                "tipoMovimiento", "DEPOSITO",
                "valor", new BigDecimal("200.00")
        );
        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.data.saldo").value(700.0));
    }

    @Test
    void createMovimiento_retiro_returns201() throws Exception {
        // Crear cuenta con saldo suficiente
        Map<String, Object> cuentaBody = Map.of(
                "numeroCuenta", "RET001",
                "tipoCuenta", "AHORRO",
                "saldoInicial", new BigDecimal("1000.00"),
                "clienteId", 10,
                "estado", true
        );
        mockMvc.perform(post("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaBody)));

        // Retiro (valor negativo)
        Map<String, Object> movBody = Map.of(
                "numeroCuenta", "RET001",
                "tipoMovimiento", "RETIRO",
                "valor", new BigDecimal("-300.00")
        );
        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tipoMovimiento").value("RETIRO"))
                .andExpect(jsonPath("$.data.saldo").value(700.0));
    }

    @Test
    void createMovimiento_saldoInsuficiente_returns409() throws Exception {
        Map<String, Object> cuentaBody = Map.of(
                "numeroCuenta", "SALDO001",
                "tipoCuenta", "AHORRO",
                "saldoInicial", new BigDecimal("100.00"),
                "clienteId", 10,
                "estado", true
        );
        mockMvc.perform(post("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaBody)));

        Map<String, Object> movBody = Map.of(
                "numeroCuenta", "SALDO001",
                "tipoMovimiento", "RETIRO",
                "valor", new BigDecimal("-500.00")
        );
        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movBody)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getReporte_returns200() throws Exception {
        String hoy = LocalDate.now().toString();
        mockMvc.perform(get("/api/reportes")
                        .param("clienteId", "10")
                        .param("fechaInicio", hoy)
                        .param("fechaFin", hoy))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.clienteId").value(10))
                .andExpect(jsonPath("$.data.cuentas").isArray());
    }

    @Test
    void getReporte_clienteInexistente_returns404() throws Exception {
        String hoy = LocalDate.now().toString();
        mockMvc.perform(get("/api/reportes")
                        .param("clienteId", "9999")
                        .param("fechaInicio", hoy)
                        .param("fechaFin", hoy))
                .andExpect(status().isNotFound());
    }
}
