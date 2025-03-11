package com.hsbc.transaction.domain.context.transaction;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

/**
 * @author ZhMM
 * @since 2025/3/10 21:58
 **/
@Data
@Validated
@AllArgsConstructor
public class TransactionCreateCommand {

    @Positive(message = "Amount must be positive")
    private Double amount;

    @Pattern(regexp = "^(INCOME|EXPENSE)$", message = "type must be INCOME or EXPENSE")
    private String type;

    @Length(max = 100, message = "description max length is 100")
    private String description;
}
