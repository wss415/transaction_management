package com.hsbc.transaction.application.usecase.query;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhMM
 * @since 2025/3/10 22:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableResult<T> {

    private int currentPage;
    private int pageSize;
    private int total;
    private int totalPages;
    private List<T> items;

    public PageableResult(int currentPage, int pageSize, int total, List<T> items) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPages = total <= 0 ? 0 : (total + pageSize - 1) / pageSize;
        this.items = items;
    }
}
