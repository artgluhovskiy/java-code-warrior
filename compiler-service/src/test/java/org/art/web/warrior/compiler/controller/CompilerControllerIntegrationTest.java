package org.art.web.warrior.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.art.web.warrior.compiler.dto.ServiceRequestDto;
import org.art.web.warrior.compiler.dto.ServiceResponseDto;
import org.art.web.warrior.compiler.model.CompilationStatus;
import org.art.web.warrior.compiler.service.CustomByteClassLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.art.web.warrior.compiler.CommonTestConstants.*;
import static org.art.web.warrior.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;
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

    private static Kryo kryo;

    private static ObjectMapper mapper;

    @BeforeAll
    static void initAll() {
        kryo = new Kryo();
        kryo.register(ServiceResponseDto.class, 10);
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request. Expects - application/x-kryo. Request params. Simple empty class (no errors)")
    void test0() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        MvcResult result = mockMvc.perform(
                get("/compile/src/params?src={src}&classname={classname}",
                        URLEncoder.encode(src, StandardCharsets.UTF_8.name()),
                        URLEncoder.encode(className, StandardCharsets.UTF_8.name()))
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(CompilationStatus.SUCCESS.getStatus(), compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request params. Simple empty class (no errors)")
    void test1() throws Exception {
        String className = "TestClass2";
        String src = "class TestClass2{}";
        MvcResult result = mockMvc.perform(
                post(COMP_PARAM_ENDPOINT)
                        .param(SRC_PARAM, src)
                        .param(CLASSNAME_PARAM, className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(CompilationStatus.SUCCESS.getStatus(), compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request params. Simple empty class (comp error)")
    void test2() throws Exception {
        String className = "TestClass3";
        String src = "private class TestClass3{}";
        MvcResult result = mockMvc.perform(
                post(COMP_PARAM_ENDPOINT)
                        .param(SRC_PARAM, src)
                        .param(CLASSNAME_PARAM, className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertAll(() -> assertEquals(CompilationStatus.ERROR.getStatus(), compResponse.getCompilerStatus()),
                () -> assertEquals(src, compResponse.getSrcCode()),
                () -> assertEquals(-1, compResponse.getCompilerStatusCode()),
                () -> assertEquals("modifier private not allowed here", compResponse.getCompilerMessage()),
                () -> assertEquals("compiler.err.mod.not.allowed.here", compResponse.getCompilerErrorCode()),
                () -> assertEquals(1, compResponse.getErrorCodeLine()),
                () -> assertEquals(9, compResponse.getErrorColumnNumber()),
                () -> assertEquals(8, compResponse.getErrorPosition()));
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request params. Empty 'src'")
    void test3() throws Exception {
        String className = "TestClass4";
        MvcResult result = mockMvc.perform(
                post(COMP_PARAM_ENDPOINT)
                        .param(CLASSNAME_PARAM, className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(className, compResponse.getClassName());
        assertEquals("Invalid request data!", compResponse.getMessage());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request params. Different class names")
    void test4() throws Exception {
        String className = "TestClass5";
        String src = "class TestClass6{}";
        MvcResult result = mockMvc.perform(
                post(COMP_PARAM_ENDPOINT)
                        .param(SRC_PARAM, src)
                        .param(CLASSNAME_PARAM, className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(CompilationStatus.SUCCESS.getStatus(), compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNull(clazz);
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request entity. Simple empty class (no errors)")
    void test5() throws Exception {
        String className = "TestClass7";
        String src = "class TestClass7{}";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(new ServiceRequestDto(className, src)))
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        ServiceResponseDto compResponse = (ServiceResponseDto) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(CompilationStatus.SUCCESS.getStatus(), compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompiledClass());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }
}
