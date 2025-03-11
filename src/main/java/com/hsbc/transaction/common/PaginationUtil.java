package com.hsbc.transaction.common;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {
    public static List<Integer> generatePagination(int currentPage, int totalPages) {
        List<Integer> pages = new ArrayList<>();
        int delta = 2; // 当前页前后显示的页码数
        int left = currentPage - delta;
        int right = currentPage + delta + 1;

        List<Integer> range = new ArrayList<>();
        int last = 0;

        for (int i = 1; i <= totalPages; i++) {
            if (i == 1 || i == totalPages || (i >= left && i < right)) {
                if (last != 0 && i - last != 1) {
                    range.add(null); // 添加分隔符
                }
                range.add(i);
                last = i;
            }
        }
        return range;
    }
}