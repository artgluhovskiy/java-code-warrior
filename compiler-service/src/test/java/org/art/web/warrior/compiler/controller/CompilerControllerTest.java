package org.art.web.warrior.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
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
        kryo.register(CompilationResponse.class, 10);
        kryo.register(CompilationUnitDto.class, 11);
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

        CompilationUnit mockUnitResult = new CompilationUnit(className, src);
        mockUnitResult.setCompiledClassBytes(mockCompiledData);
        compResult.setCompUnitResults(singletonMap(className, mockUnitResult));

        when(compilationService.compileUnits(singletonList(unit))).thenReturn(compResult);

        CompilationRequest compReq = new CompilationRequest(singletonList(new CompilationUnitDto(className, src)));
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
        assertEquals(className, compUnitResult.getClassName());
        assertEquals(src, compUnitResult.getSrcCode());
        assertArrayEquals(mockCompiledData, compUnitResult.getCompiledClassBytes());

        verify(compilationService).compileUnits(singletonList(unit));
    }

    @Test
    @DisplayName("Compilation POST request: bad request params (no 'src')")
    void test2() throws Exception {
        String className = "TestClass4";

        CompilationRequest compReq = new CompilationRequest(singletonList(new CompilationUnitDto(className, null)));
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

        verify(compilationService, never()).compileUnits(anyList());
    }
}
