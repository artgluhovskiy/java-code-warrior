package org.art.web.warrior.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationReq;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitReq;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;
import static org.art.web.warrior.compiler.CommonTestConstants.COMP_ENTITY_ENDPOINT;
import static org.art.web.warrior.compiler.ServiceCommonConstants.COMPILER_SERVICE_OK_MESSAGE;
import static org.art.web.warrior.compiler.ServiceCommonConstants.REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompilerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompilationService compilationService;

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
    @DisplayName("Compiler Service Ping Test (json content type)")
    void test0() throws Exception {
        MvcResult result = mockMvc.perform(get("/compile/ping")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        assertTrue(response.contains(COMPILER_SERVICE_OK_MESSAGE));

        verify(compilationService, never()).compileUnits(anyList());
    }

    @Test
    @DisplayName("Compilation POST request: Simple empty class (expects - application/x-kryo)")
    void test1() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        byte[] mockCompiledData = new byte[10];

        CompilationUnit unit = new CompilationUnit(className, src);
        CompilationResult compResult = new CompilationResult(ServiceResponseStatus.SUCCESS);

        CompilationUnitResp mockUnitResult = new CompilationUnitResp(className, src, mockCompiledData);
        compResult.setCompUnitResults(singletonMap(className, mockUnitResult));

        when(compilationService.compileUnits(singletonList(unit))).thenReturn(compResult);

        CompilationReq compReq = new CompilationReq(singletonList(new CompilationUnitReq(className, src)));
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
        assertEquals(className, unitResult.getClassName());
        assertEquals(src, unitResult.getSrcCode());
        assertArrayEquals(mockCompiledData, unitResult.getCompiledClassBytes());

        verify(compilationService).compileUnits(singletonList(unit));
    }

    @Test
    @DisplayName("Compilation POST request: bad request params (no 'src')")
    void test2() throws Exception {
        String className = "TestClass4";

        CompilationReq compReq = new CompilationReq(singletonList(new CompilationUnitReq(className, null)));
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

        verify(compilationService, never()).compileUnits(anyList());
    }
}
