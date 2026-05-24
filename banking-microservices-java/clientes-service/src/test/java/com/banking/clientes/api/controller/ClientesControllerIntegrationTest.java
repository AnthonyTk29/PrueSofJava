package com.banking.clientes.api.controller;

import com.banking.clientes.application.event.ClienteEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClientesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteEventPublisher eventPublisher;

    // Necesario para que el contexto arranque sin RabbitMQ real
    // (RabbitAutoConfiguration está excluida en application-test.yml,
    // pero RabbitMqConfig aún requiere ConnectionFactory para RabbitTemplate)
    @MockBean
    private ConnectionFactory connectionFactory;

    @Test
    void createCliente_returns201() throws Exception {
        doNothing().when(eventPublisher).publishCreated(any());

        Map<String, Object> body = Map.of(
                "nombre", "Test User",
                "genero", "Masculino",
                "edad", 25,
                "identificacion", "9999999999",
                "direccion", "Calle Falsa 123",
                "telefono", "0999999999",
                "contrasena", "secret",
                "estado", true
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.nombre").value("Test User"))
                .andExpect(jsonPath("$.data.identificacion").value("9999999999"))
                .andExpect(jsonPath("$.data.estado").value(true));
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/clientes/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updateCliente_returns200() throws Exception {
        doNothing().when(eventPublisher).publishCreated(any());
        doNothing().when(eventPublisher).publishUpdated(any());

        // Crear primero
        Map<String, Object> createBody = Map.of(
                "nombre", "Original",
                "genero", "Masculino",
                "edad", 30,
                "identificacion", "7777777777",
                "direccion", "Av. Principal",
                "telefono", "0988888888",
                "contrasena", "pass123",
                "estado", true
        );
        String createResult = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBody)))
                .andReturn().getResponse().getContentAsString();

        Integer clienteId = objectMapper.readTree(createResult).path("data").path("clienteId").asInt();

        // Actualizar
        Map<String, Object> updateBody = Map.of(
                "nombre", "Actualizado",
                "genero", "Masculino",
                "edad", 31,
                "identificacion", "7777777777",
                "direccion", "Av. Nueva",
                "telefono", "0988888888",
                "contrasena", "newpass",
                "estado", true
        );
        mockMvc.perform(put("/api/clientes/" + clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("Actualizado"));
    }

    @Test
    void patchEstado_returns200() throws Exception {
        doNothing().when(eventPublisher).publishCreated(any());
        doNothing().when(eventPublisher).publishUpdated(any());

        Map<String, Object> createBody = Map.of(
                "nombre", "Para Deshabilitar",
                "genero", "Femenino",
                "edad", 28,
                "identificacion", "6666666666",
                "direccion", "Calle 10",
                "telefono", "0977777777",
                "contrasena", "pass",
                "estado", true
        );
        String createResult = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBody)))
                .andReturn().getResponse().getContentAsString();

        Integer clienteId = objectMapper.readTree(createResult).path("data").path("clienteId").asInt();

        mockMvc.perform(patch("/api/clientes/" + clienteId + "/estado?estado=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.estado").value(false));
    }

    @Test
    void createCliente_invalidBody_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "",
                "genero", "Masculino",
                "edad", 25,
                "identificacion", "8888888888",
                "direccion", "Calle",
                "telefono", "099",
                "contrasena", "pass",
                "estado", true
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void createCliente_missingFields_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "genero", "Masculino",
                "edad", 25
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
