package com.hsbc.transaction.adapter.driven;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * ID生成器
 *
 * @author ZhMM
 * @since 2025/3/10 21:24
 **/
@Component
public class IdGenerator {

    //生成器技术实现
    private final AtomicLong idGenerator = new AtomicLong(1000);

    public String generate() {
        return idGenerator.incrementAndGet() + "";
    }

    public String generate(String prefix) {
        return prefix + generate();
    }
}