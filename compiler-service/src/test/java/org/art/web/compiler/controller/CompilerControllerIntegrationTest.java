package org.art.web.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.service.CustomByteClassLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.art.web.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompilerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static Kryo KRYO;

    @BeforeAll
    static void initAll() {
        KRYO = new Kryo();
        KRYO.register(ServiceResponseDto.class, 10);
    }

    @Test
    @DisplayName("Compilation GET request: Simple empty class (expects - application/x-kryo)")
    void test0() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        MvcResult result = mockMvc.perform(
                get("/compile/src?src={src}&classname={classname}",
                        URLEncoder.encode(src, StandardCharsets.UTF_8.name()),
                        URLEncoder.encode(className, StandardCharsets.UTF_8.name()))
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) KRYO.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals("Success", compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("Compilation POST request: Simple empty class (expects - application/x-kryo)")
    void test1() throws Exception {
        String className = "TestClass2";
        String src = "class TestClass2{}";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("src", src)
                        .param("classname", className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) KRYO.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals("Success", compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("Compilation POST request: Simple empty class with comp error (expects - application/x-kryo)")
    void test2() throws Exception {
        String className = "TestClass3";
        String src = "private class TestClass3{}";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("src", src)
                        .param("classname", className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) KRYO.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertAll(() -> assertEquals("Error", compResponse.getCompilerStatus()),
                () -> assertEquals(src, compResponse.getSrcCode()),
                () -> assertEquals(-1, compResponse.getCompilerStatusCode()),
                () -> assertEquals("modifier private not allowed here", compResponse.getCompilerMessage()),
                () -> assertEquals("compiler.err.mod.not.allowed.here", compResponse.getCompilerErrorCode()),
                () -> assertEquals(1, compResponse.getErrorCodeLine()),
                () -> assertEquals(9, compResponse.getErrorColumnNumber()),
                () -> assertEquals(8, compResponse.getErrorPosition()));
    }

    @Test
    @DisplayName("Compilation POST request: bad request params (no 'src')")
    void test3() throws Exception {
        String className = "TestClass4";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("classname", className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) KRYO.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(className, compResponse.getClassName());
        assertEquals("Invalid request data!", compResponse.getMessage());
    }

    @Test
    @DisplayName("Compilation POST request: bad request params (diff class names)")
    void test4() throws Exception {
        String className = "TestClass5";
        String src = "class TestClass6{}";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("src", src)
                        .param("classname", className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) KRYO.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals("Success", compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNull(clazz);
    }
}
