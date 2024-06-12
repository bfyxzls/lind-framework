package com.lind.common.delegate;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @author lind
 * @date 2024/6/11 9:46
 * @since 1.0.0
 */
public class ConsumerTest {
    @Test
    public void test1(){
        Consumer<String> consumer = (str) -> System.out.println("Hello, " + str);
        consumer.accept("World");

    }
}
