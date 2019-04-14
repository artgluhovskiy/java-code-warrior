package org.art.web.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.model.CharSeqCompilationUnit;
import org.art.web.compiler.model.CommonCompilationResult;
import org.art.web.compiler.model.api.CompilationStatus;
import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.art.web.compiler.service.ServiceCommonConstants.COMPILER_SERVICE_OK_MESSAGE;
import static org.art.web.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    private static Kryo KRYO;

    @BeforeAll
    static void initAll() {
        KRYO = new Kryo();
        KRYO.register(ServiceResponseDto.class, 10);
    }

    @Test
    @DisplayName("Compiler Service Ping Test (json content type)")
    void test0() throws Exception {
        MvcResult result = mockMvc.perform(get("/compile/ping")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        assertTrue(response.contains(COMPILER_SERVICE_OK_MESSAGE));

        verify(compilationService, never()).compileUnit(any(CompilationUnit.class));
    }

    @Test
    @DisplayName("Compilation POST request: Simple empty class (expects - application/x-kryo)")
    void test1() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        byte[] mockCompiledData = new byte[10];

        CompilationUnit unit = new CharSeqCompilationUnit(className, src);
        CommonCompilationResult compResult = new CommonCompilationResult(CompilationStatus.SUCCESS);
        compResult.setClassName(className);
        compResult.setSrcCode(src);
        compResult.setCompiledClassBytes(mockCompiledData);

        when(compilationService.compileUnit(unit)).thenReturn(compResult);

        MvcResult result = mockMvc.perform(
                post("/compile/src/params")
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
        assertAll(() -> assertEquals("Success", compResponse.getCompilerStatus()),
                () -> assertEquals(className, compResponse.getClassName()),
                () -> assertEquals(src, compResponse.getSrcCode()),
                () -> assertArrayEquals(mockCompiledData, compResponse.getCompiledClass()));

        verify(compilationService).compileUnit(unit);
    }

    @Test
    @DisplayName("Compilation POST request: bad request params (no 'src')")
    void test2() throws Exception {
        String className = "TestClass4";

        MvcResult result = mockMvc.perform(
                post("/compile/src/params")
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

        verify(compilationService, never()).compileUnit(any(CompilationUnit.class));
    }
}
