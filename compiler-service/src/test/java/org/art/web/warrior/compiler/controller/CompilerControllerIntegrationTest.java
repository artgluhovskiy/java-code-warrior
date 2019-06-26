package org.art.web.warrior.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.art.web.warrior.commons.CustomByteClassLoader;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
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

import java.util.List;

import static java.util.Collections.singletonList;
import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;
import static org.art.web.warrior.compiler.CommonTestConstants.COMP_ENTITY_ENDPOINT;
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
        kryo.register(CompilationResponse.class, 10);
        kryo.register(CompilationUnitDto.class, 11);
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request. Expects - application/x-kryo. Simple empty class (no errors)")
    void test0() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        List<CompilationUnitDto> compUnit = singletonList(new CompilationUnitDto(className, src));
        CompilationRequest compReq = new CompilationRequest(compUnit);
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(compReq))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompilationResponse compilationResponse = (CompilationResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResponse);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compilationResponse.getCompilerStatus());
        CompilationUnitDto compUnitResult = compilationResponse.getCompUnitResults().get(className);
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compUnitResult.getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Simple empty class (no errors)")
    void test1() throws Exception {
        String className = "TestClass2";
        String src = "class TestClass2{}";
        List<CompilationUnitDto> compUnit = singletonList(new CompilationUnitDto(className, src));
        CompilationRequest compReq = new CompilationRequest(compUnit);
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(compReq))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompilationResponse compilationResponse = (CompilationResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResponse);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compilationResponse.getCompilerStatus());
        CompilationUnitDto compUnitResult = compilationResponse.getCompUnitResults().get(className);
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compUnitResult.getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Simple empty class (comp error)")
    void test2() throws Exception {
        String className = "TestClass3";
        String src = "private class TestClass3{}";
        List<CompilationUnitDto> compUnit = singletonList(new CompilationUnitDto(className, src));
        CompilationRequest compReq = new CompilationRequest(compUnit);
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(compReq))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompilationResponse compilationResponse = (CompilationResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResponse);
        assertAll(() -> assertEquals(ServiceResponseStatus.COMPILATION_ERROR.getStatusId(), compilationResponse.getCompilerStatus()),
                () -> assertEquals(-2, compilationResponse.getCompilerStatusCode()),
                () -> assertEquals("modifier private not allowed here", compilationResponse.getCompilerMessage()),
                () -> assertEquals("compiler.err.mod.not.allowed.here", compilationResponse.getCompilerErrorCode()),
                () -> assertEquals(1, compilationResponse.getErrorCodeLine()),
                () -> assertEquals(9, compilationResponse.getErrorColumnNumber()),
                () -> assertEquals(8, compilationResponse.getErrorPosition()));
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Empty 'src'")
    void test3() throws Exception {
        String className = "TestClass4";
        List<CompilationUnitDto> compUnit = singletonList(new CompilationUnitDto(className, null));
        CompilationRequest compReq = new CompilationRequest(compUnit);
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(compReq))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        Object responseObject = kryo.readClassAndObject(new Input(binResponseData));
        assertTrue(responseObject instanceof CommonApiError);
        CommonApiError compilationErrorResponse = (CommonApiError) responseObject;
        assertNotNull(compilationErrorResponse);
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusId(), compilationErrorResponse.getRespStatus());
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusCode(), compilationErrorResponse.getRespStatusCode());
        assertNotNull(compilationErrorResponse.getMessage());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Different class names")
    void test4() throws Exception {
        String className = "TestClass5";
        String src = "class TestClass6{}";
        List<CompilationUnitDto> compUnit = singletonList(new CompilationUnitDto(className, src));
        CompilationRequest compReq = new CompilationRequest(compUnit);
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .content(mapper.writeValueAsString(compReq))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        Object responseObject = kryo.readClassAndObject(new Input(binResponseData));
        assertTrue(responseObject instanceof CommonApiError);
        CommonApiError compilationErrorResponse = (CommonApiError) responseObject;
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusId(), compilationErrorResponse.getRespStatus());
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusCode(), compilationErrorResponse.getRespStatusCode());
        assertNotNull(compilationErrorResponse.getMessage());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request entity. Simple empty class (no errors)")
    void test5() throws Exception {
        String className = "TestClass7";
        String src = "class TestClass7{}";
        List<CompilationUnitDto> compUnit = singletonList(new CompilationUnitDto(className, src));
        CompilationRequest compReq = new CompilationRequest(compUnit);
        MvcResult result = mockMvc.perform(
                post(COMP_ENTITY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(compReq))
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binResponseData = result.getResponse().getContentAsByteArray();
        assertNotNull(binResponseData);
        CompilationResponse compilationResponse = (CompilationResponse) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResponse);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compilationResponse.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compilationResponse.getCompUnitResults().get(className).getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }
}
