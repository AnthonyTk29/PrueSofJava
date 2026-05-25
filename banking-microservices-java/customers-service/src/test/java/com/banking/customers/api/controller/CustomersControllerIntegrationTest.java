package com.banking.customers.api.controller;

import com.banking.customers.application.event.CustomerEventPublisher;
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
class CustomersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerEventPublisher eventPublisher;

    @MockBean
    private ConnectionFactory connectionFactory;

    @Test
    void createCustomer_returns201() throws Exception {
        doNothing().when(eventPublisher).publishCreated(any());

        Map<String, Object> body = Map.of(
                "name", "Test User",
                "gender", "Male",
                "age", 25,
                "identification", "9999999999",
                "address", "Fake Street 123",
                "phone", "0999999999",
                "password", "secret",
                "active", true
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.identification").value("9999999999"))
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updateCustomer_returns200() throws Exception {
        doNothing().when(eventPublisher).publishCreated(any());
        doNothing().when(eventPublisher).publishUpdated(any());

        Map<String, Object> createBody = Map.of(
                "name", "Original",
                "gender", "Male",
                "age", 30,
                "identification", "7777777777",
                "address", "Main Ave",
                "phone", "0988888888",
                "password", "pass123",
                "active", true
        );
        String createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBody)))
                .andReturn().getResponse().getContentAsString();

        Integer clientId = objectMapper.readTree(createResult).path("data").path("clientId").asInt();

        Map<String, Object> updateBody = Map.of(
                "name", "Updated",
                "gender", "Male",
                "age", 31,
                "identification", "7777777777",
                "address", "New Ave",
                "phone", "0988888888",
                "password", "newpass",
                "active", true
        );
        mockMvc.perform(put("/api/customers/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated"));
    }

    @Test
    void patchStatus_returns200() throws Exception {
        doNothing().when(eventPublisher).publishCreated(any());
        doNothing().when(eventPublisher).publishUpdated(any());

        Map<String, Object> createBody = Map.of(
                "name", "To Disable",
                "gender", "Female",
                "age", 28,
                "identification", "6666666666",
                "address", "Street 10",
                "phone", "0977777777",
                "password", "pass",
                "active", true
        );
        String createResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBody)))
                .andReturn().getResponse().getContentAsString();

        Integer clientId = objectMapper.readTree(createResult).path("data").path("clientId").asInt();

        mockMvc.perform(patch("/api/customers/" + clientId + "/status?active=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active").value(false));
    }

    @Test
    void createCustomer_invalidBody_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "",
                "gender", "Male",
                "age", 25,
                "identification", "8888888888",
                "address", "Street",
                "phone", "099",
                "password", "pass",
                "active", true
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void createCustomer_missingFields_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "gender", "Male",
                "age", 25
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
