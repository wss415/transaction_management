package com.hsbc.transaction.adapter.driving.transaction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hsbc.transaction.application.usecase.query.PageableResult;
import com.hsbc.transaction.application.usecase.query.transaction.TransactionQueryService;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;


/**
 * @author ZhMM
 * @since 2025/3/11 20:20
 **/
@ExtendWith(MockitoExtension.class)
class TransactionUIControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionQueryService transactionQueryService;

    @InjectMocks
    private TransactionUIController transactionUIController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionUIController).build();
    }

    @Test
    public void queryPageableTransaction_ValidInputs_ReturnsView() throws Exception {
        PageableResult<Transaction> pageableResult
            = new PageableResult<>(1, 2, 2, Collections.emptyList());
        when(transactionQueryService.queryPageableTransaction(1, 2)).thenReturn(pageableResult);

        mockMvc.perform(get("/transactions"))
            .andExpect(status().isOk())
            .andExpect(view().name("transaction-list"));
    }

    @Test
    public void queryPageableTransaction_InvalidPageIndex_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionUIController.queryPageableTransaction(new ExtendedModelMap(), 0, 2);
        });
    }

    @Test
    public void queryPageableTransaction_InvalidPageSize_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionUIController.queryPageableTransaction(new ExtendedModelMap(), 1, 0);
        });
    }
}