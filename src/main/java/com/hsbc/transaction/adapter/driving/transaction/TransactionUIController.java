package com.hsbc.transaction.adapter.driving.transaction;

import com.hsbc.transaction.application.usecase.query.transaction.TransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZhMM
 * @since 2025/3/10 23:56
 **/
@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionUIController {

    private final TransactionQueryService transactionQueryService;

    @GetMapping
    public String queryPageableTransaction(Model model,
                                           @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Assert.isTrue(pageIndex > 0, "pageIndex must large 0");
        Assert.isTrue(pageSize > 0, "pageSize must large 0");
        model.addAttribute("page", transactionQueryService.queryPageableTransaction(pageIndex, pageSize));
        return "transaction-list";
    }
}
