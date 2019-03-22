package org.art.web.compiler.controller;

import org.apache.commons.lang3.StringUtils;
import org.art.web.compiler.service.CustomByteClassLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.art.web.compiler.service.ServiceCommonConstants.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompilerControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(CompilerController.class)
                .setMessageConverters()
    .alwaysExpect(status().isOk())
                .build();
    }

    @Test
    @DisplayName("Compiler Service Ping Test (json content type)")
    void test0() throws Exception {
        mockMvc.perform(get("/compile/ping")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(QUOTE_CH + COMPILER_SERVICE_OK_MESSAGE + QUOTE_CH));
    }

    @Test
    @DisplayName("Compiler Service Ping Test (x-kryo content type)")
    void test1() throws Exception {
        MvcResult result = mockMvc.perform(get("/compile/ping")
                .accept(KRYO_MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andExpect(content().string(COMPILER_SERVICE_OK_MESSAGE))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
    }


}
