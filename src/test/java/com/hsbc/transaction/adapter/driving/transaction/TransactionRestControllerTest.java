package com.hsbc.transaction.adapter.driving.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hsbc.transaction.application.usecase.command.transaction.TransactionCommandService;
import com.hsbc.transaction.application.usecase.query.transaction.TransactionQueryService;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author ZhMM
 * @since 2025/3/11 0:51
 **/
class TransactionRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionCommandService transactionCommandService;

    @Mock
    private TransactionQueryService transactionQueryService;

    @InjectMocks
    private TransactionRestController transactionRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionRestController).build();
    }

    @Test
    public void createTransaction_ValidCommand_ShouldReturnCreated() throws Exception {
        Transaction transaction = Transaction.builder()
            .id("1")
            .amount(100.0)
            .type(null)
            .description("Test Description")
            .createAt(LocalDateTime.now())
            .lastUpdateAt(LocalDateTime.now())
            .build();
        when(transactionCommandService.createTransaction(any(TransactionCreateCommand.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.valueOf("application/json"))
                .content("{\"amount\":100.0,\"type\":\"INCOME\",\"description\":\"Test Description\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.amount").value(100.0))
            .andExpect(jsonPath("$.description").value("Test Description"));

        verify(transactionCommandService, times(1))
            .createTransaction(any(TransactionCreateCommand.class));
    }

    @Test
    public void deleteTransaction_ValidId_ShouldReturnOk() throws Exception {
        doNothing().when(transactionCommandService).deleteTransactionById("1");

        mockMvc.perform(delete("/api/v1/transactions/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));

        verify(transactionCommandService, times(1)).deleteTransactionById("1");
    }

    @Test
    public void updateTransaction_ValidIdAndCommand_ShouldReturnOk() throws Exception {
        TransactionCreateCommand command
            = new TransactionCreateCommand(200.0, "EXPENSE", "Updated Description");

        Transaction transaction = Transaction.builder()
            .id("1")
            .amount(200.0)
            .type(null)
            .description("Updated Description")
            .createAt(LocalDateTime.now())
            .lastUpdateAt(LocalDateTime.now())
            .build();
        when(transactionCommandService.updateTransactionById(eq("1"),
            any(TransactionCreateCommand.class))).thenReturn(transaction);

        mockMvc.perform(put("/api/v1/transactions/1")
                .contentType("application/json")
                .content("{\"amount\":200.0,\"type\":\"EXPENSE\",\"description\":\"Updated Description\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.amount").value(200.0))
            .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(transactionCommandService, times(1))
            .updateTransactionById(eq("1"), any(TransactionCreateCommand.class));
    }

    @Test
    public void getTransaction_ValidId_ShouldReturnOk() throws Exception {
        Transaction transaction = Transaction.builder()
            .id("1")
            .amount(100.0)
            .type(null)
            .description("Test Description")
            .createAt(LocalDateTime.now())
            .lastUpdateAt(LocalDateTime.now())
            .build();
        when(transactionQueryService.getTransactionById("1")).thenReturn(transaction);

        mockMvc.perform(get("/api/v1/transactions/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.amount").value(100.0))
            .andExpect(jsonPath("$.description").value("Test Description"));

        verify(transactionQueryService, times(1)).getTransactionById("1");
    }
}