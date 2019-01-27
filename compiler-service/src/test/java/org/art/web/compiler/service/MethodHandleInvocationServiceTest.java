package org.art.web.compiler.service;

import org.art.web.compiler.model.CommonMethodDescriptor;
import org.art.web.compiler.service.api.MethodInvocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MethodHandleInvocationServiceTest {

    @Autowired
    private MethodInvocationService invocationService;

    @Test
    @DisplayName("Method invocation: \"hello\".toUppercase()")
    void test0() throws Throwable {
        String target = "hello";
        String result = "HELLO";
        CommonMethodDescriptor methodDescriptor = new CommonMethodDescriptor(target, "toUpperCase");
        Class<String> retType = String.class;
        methodDescriptor.setReturnType(retType);
        assertEquals(result, retType.cast(invocationService.invokeMethod(methodDescriptor)));
    }
}