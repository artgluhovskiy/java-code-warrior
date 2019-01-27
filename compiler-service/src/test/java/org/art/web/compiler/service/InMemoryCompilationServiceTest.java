package org.art.web.compiler.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

@DisplayName("InMemoryCompilationService Tests")
class InMemoryCompilationServiceTest {

    private static final String TEST_DATA_PATH = "compilation-service/code-samples.txt";

    @BeforeAll
    static void initAll() throws IOException {
        InputStream input = InMemoryCompilationServiceTest.class.getClassLoader().getResourceAsStream(TEST_DATA_PATH);
        System.out.println("Available: " + input.available());
        input.close();
    }

    @Test
    void test0() {
        System.out.println("Hello");
    }


}