package com.hsbc.transaction.adapter.driving.transaction;

import com.hsbc.transaction.application.usecase.command.transaction.TransactionCommandService;
import com.hsbc.transaction.application.usecase.query.transaction.TransactionQueryService;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhMM
 * @since 2025/3/10 22:50
 **/
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionCommandService transactionCommandService;

    private final TransactionQueryService transactionQueryService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionCreateCommand command) {
        Transaction transaction = transactionCommandService.createTransaction(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable(name = "id") String id) {
        transactionCommandService.deleteTransactionById(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable(name = "id") String id,
                                                         @Valid @RequestBody TransactionCreateCommand command) {
        Transaction transaction = transactionCommandService.updateTransactionById(id, command);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable(name = "id") String id) {
        Transaction transactionById = transactionQueryService.getTransactionById(id);
        return ResponseEntity.ok(transactionById);
    }

}
