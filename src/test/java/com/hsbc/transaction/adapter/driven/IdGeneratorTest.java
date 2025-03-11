package com.hsbc.transaction.adapter.driven;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IdGeneratorTest {

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void generate_ShouldReturnIncrementedId() {
        String id = idGenerator.generate();
        assertEquals("1001", id); // 第一次调用应返回1001
    }

    @Test
    public void generateWithPrefix_ShouldReturnIdWithPrefix() {
        String id = idGenerator.generate("pre");
        assertEquals("pre1002", id); // 第二次调用应返回1002，加上前缀"pre"
    }
}
