package org.art.web.warrior.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.art.web.warrior.commons.compiler.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;
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

import static java.util.Collections.singletonList;
import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;
import static org.art.web.warrior.compiler.CommonTestConstants.COMP_ENTITY_ENDPOINT;
import static org.art.web.warrior.compiler.ServiceCommonConstants.REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
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
        kryo.register(CompServiceResponse.class, 10);
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request. Expects - application/x-kryo. Simple empty class (no errors)")
    void test0() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(singletonList(new CompServiceUnitRequest(className, src))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompServiceResponse compResponse = (CompServiceResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compResponse.getCompilerStatus());
        CompServiceUnitResponse unitResult = compResponse.getCompUnitResults().get(className);
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, unitResult.getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Simple empty class (no errors)")
    void test1() throws Exception {
        String className = "TestClass2";
        String src = "class TestClass2{}";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(singletonList(new CompServiceUnitRequest(className, src))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompServiceResponse compResponse = (CompServiceResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compResponse.getCompilerStatus());
        CompServiceUnitResponse unitResult = compResponse.getCompUnitResults().get(className);
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, unitResult.getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Simple empty class (comp error)")
    void test2() throws Exception {
        String className = "TestClass3";
        String src = "private class TestClass3{}";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(singletonList(new CompServiceUnitRequest(className, src))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompServiceResponse compResponse = (CompServiceResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertAll(() -> assertEquals(ServiceResponseStatus.COMPILATION_ERROR.getStatusId(), compResponse.getCompilerStatus()),
                () -> assertEquals(-2, compResponse.getCompilerStatusCode()),
                () -> assertEquals("modifier private not allowed here", compResponse.getCompilerMessage()),
                () -> assertEquals("compiler.err.mod.not.allowed.here", compResponse.getCompilerErrorCode()),
                () -> assertEquals(1, compResponse.getErrorCodeLine()),
                () -> assertEquals(9, compResponse.getErrorColumnNumber()),
                () -> assertEquals(8, compResponse.getErrorPosition()));
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Empty 'src'")
    void test3() throws Exception {
        String className = "TestClass4";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(singletonList(new CompServiceUnitRequest(className, null))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompServiceResponse compResponse = (CompServiceResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(className, compResponse.getCompUnitResults().get(className).getClassName());
        assertEquals(REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE, compResponse.getMessage());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Different class names")
    void test4() throws Exception {
        String className = "TestClass5";
        String src = "class TestClass6{}";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(singletonList(new CompServiceUnitRequest(className, src))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompServiceResponse compResponse = (CompServiceResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        CompServiceUnitResponse unitResult = compResponse.getCompUnitResults().get(className);
        assertNotNull(unitResult);
        assertEquals(className, unitResult.getClassName());
        assertEquals(src, unitResult.getSrcCode());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request entity. Simple empty class (no errors)")
    void test5() throws Exception {
        String className = "TestClass7";
        String src = "class TestClass7{}";
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(singletonList(new CompServiceUnitRequest(className, src))))
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompServiceResponse compResponse = (CompServiceResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compResponse);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compResponse.getCompUnitResults().get(className).getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }
}
