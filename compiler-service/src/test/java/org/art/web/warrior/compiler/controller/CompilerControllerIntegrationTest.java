package org.art.web.warrior.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationReq;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitReq;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;
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

import java.util.List;

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
        kryo.register(CompilationResp.class, 10);
        kryo.register(CompilationUnitResp.class, 11);
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request. Expects - application/x-kryo. Simple empty class (no errors)")
    void test0() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        List<CompilationUnitReq> compUnit = singletonList(new CompilationUnitReq(className, src));
        CompilationReq compReq = new CompilationReq(compUnit);
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
        CompilationResp compilationResp = (CompilationResp) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResp);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compilationResp.getCompilerStatus());
        CompilationUnitResp unitResult = compilationResp.getCompUnitResults().get(className);
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
        List<CompilationUnitReq> compUnit = singletonList(new CompilationUnitReq(className, src));
        CompilationReq compReq = new CompilationReq(compUnit);
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
        CompilationResp compilationResp = (CompilationResp) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResp);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compilationResp.getCompilerStatus());
        CompilationUnitResp unitResult = compilationResp.getCompUnitResults().get(className);
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
        List<CompilationUnitReq> compUnit = singletonList(new CompilationUnitReq(className, src));
        CompilationReq compReq = new CompilationReq(compUnit);
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
        CompilationResp compilationResp = (CompilationResp) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResp);
        assertAll(() -> assertEquals(ServiceResponseStatus.COMPILATION_ERROR.getStatusId(), compilationResp.getCompilerStatus()),
                () -> assertEquals(-2, compilationResp.getCompilerStatusCode()),
                () -> assertEquals("modifier private not allowed here", compilationResp.getCompilerMessage()),
                () -> assertEquals("compiler.err.mod.not.allowed.here", compilationResp.getCompilerErrorCode()),
                () -> assertEquals(1, compilationResp.getErrorCodeLine()),
                () -> assertEquals(9, compilationResp.getErrorColumnNumber()),
                () -> assertEquals(8, compilationResp.getErrorPosition()));
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Empty 'src'")
    void test3() throws Exception {
        String className = "TestClass4";
        List<CompilationUnitReq> compUnit = singletonList(new CompilationUnitReq(className, null));
        CompilationReq compReq = new CompilationReq(compUnit);
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
        CompilationResp compilationResp = (CompilationResp) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResp);
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusId(), compilationResp.getCompilerStatus());
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusCode(), compilationResp.getCompilerStatusCode());
        assertNotNull(compilationResp.getMessage());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Different class names")
    void test4() throws Exception {
        String className = "TestClass5";
        String src = "class TestClass6{}";
        List<CompilationUnitReq> compUnit = singletonList(new CompilationUnitReq(className, src));
        CompilationReq compReq = new CompilationReq(compUnit);
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
        CompilationResp compilationResp = (CompilationResp) kryo.readClassAndObject(new Input(binResponseData));
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusId(), compilationResp.getCompilerStatus());
        assertEquals(ServiceResponseStatus.BAD_REQUEST.getStatusCode(), compilationResp.getCompilerStatusCode());
        assertNotNull(compilationResp.getMessage());
    }

    @Test
    @DisplayName("POST request. Expects - application/x-kryo. Request entity. Simple empty class (no errors)")
    void test5() throws Exception {
        String className = "TestClass7";
        String src = "class TestClass7{}";
        List<CompilationUnitReq> compUnit = singletonList(new CompilationUnitReq(className, src));
        CompilationReq compReq = new CompilationReq(compUnit);
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
        CompilationResp compilationResp = (CompilationResp) kryo.readClassAndObject(new Input(binResponseData));
        assertNotNull(compilationResp);
        assertEquals(ServiceResponseStatus.SUCCESS.getStatusId(), compilationResp.getCompilerStatus());
        CustomByteClassLoader loader = new CustomByteClassLoader();
        loader.addClassData(className, compilationResp.getCompUnitResults().get(className).getCompiledClassBytes());
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }
}
