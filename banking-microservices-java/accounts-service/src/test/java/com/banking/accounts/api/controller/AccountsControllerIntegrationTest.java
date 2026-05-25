package com.banking.accounts.api.controller;

import com.banking.accounts.domain.entity.CustomerReadModel;
import com.banking.accounts.infrastructure.messaging.RabbitMqCustomerEventConsumer;
import com.banking.accounts.infrastructure.persistence.CustomerReadModelJpaRepository;
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
class AccountsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerReadModelJpaRepository customerReadModelJpaRepository;

    @MockBean
    private ConnectionFactory connectionFactory;

    @MockBean
    private RabbitMqCustomerEventConsumer rabbitMqCustomerEventConsumer;

    @BeforeEach
    void setUp() {
        if (!customerReadModelJpaRepository.existsByClientId(10)) {
            CustomerReadModel customer = new CustomerReadModel();
            customer.setClientId(10);
            customer.setName("Test Client");
            customer.setIdentification("5555555555");
            customer.setActive(true);
            customerReadModelJpaRepository.save(customer);
        }
    }

    @Test
    void createAccount_returns201() throws Exception {
        Map<String, Object> body = Map.of(
                "accountNumber", "TEST001",
                "accountType", "SAVINGS",
                "initialBalance", new BigDecimal("1000.00"),
                "clientId", 10,
                "active", true
        );

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.accountNumber").value("TEST001"))
                .andExpect(jsonPath("$.data.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.data.initialBalance").value(1000.00));
    }

    @Test
    void createAccount_nonExistentClient_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "accountNumber", "NOEXIST",
                "accountType", "SAVINGS",
                "initialBalance", new BigDecimal("500.00"),
                "clientId", 9999,
                "active", true
        );

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createAccount_invalidAccountType_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "accountNumber", "TIPO_MAL",
                "accountType", "INVALID",
                "initialBalance", new BigDecimal("500.00"),
                "clientId", 10,
                "active", true
        );

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/accounts/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createTransaction_deposit_returns201() throws Exception {
        Map<String, Object> accountBody = Map.of(
                "accountNumber", "MOV001",
                "accountType", "SAVINGS",
                "initialBalance", new BigDecimal("500.00"),
                "clientId", 10,
                "active", true
        );
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountBody)));

        Map<String, Object> txBody = Map.of(
                "accountNumber", "MOV001",
                "transactionType", "DEPOSIT",
                "amount", new BigDecimal("200.00")
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(txBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$.data.balance").value(700.0));
    }

    @Test
    void createTransaction_withdrawal_returns201() throws Exception {
        Map<String, Object> accountBody = Map.of(
                "accountNumber", "RET001",
                "accountType", "SAVINGS",
                "initialBalance", new BigDecimal("1000.00"),
                "clientId", 10,
                "active", true
        );
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountBody)));

        Map<String, Object> txBody = Map.of(
                "accountNumber", "RET001",
                "transactionType", "WITHDRAWAL",
                "amount", new BigDecimal("-300.00")
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(txBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.transactionType").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.data.balance").value(700.0));
    }

    @Test
    void createTransaction_insufficientBalance_returns409() throws Exception {
        Map<String, Object> accountBody = Map.of(
                "accountNumber", "SALDO001",
                "accountType", "SAVINGS",
                "initialBalance", new BigDecimal("100.00"),
                "clientId", 10,
                "active", true
        );
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountBody)));

        Map<String, Object> txBody = Map.of(
                "accountNumber", "SALDO001",
                "transactionType", "WITHDRAWAL",
                "amount", new BigDecimal("-500.00")
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(txBody)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getReport_returns200() throws Exception {
        String today = LocalDate.now().toString();
        mockMvc.perform(get("/api/reports")
                        .param("clientId", "10")
                        .param("startDate", today)
                        .param("endDate", today))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.clientId").value(10))
                .andExpect(jsonPath("$.data.accounts").isArray());
    }

    @Test
    void getReport_nonExistentClient_returns404() throws Exception {
        String today = LocalDate.now().toString();
        mockMvc.perform(get("/api/reports")
                        .param("clientId", "9999")
                        .param("startDate", today)
                        .param("endDate", today))
                .andExpect(status().isNotFound());
    }
}
