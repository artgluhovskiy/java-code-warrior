package org.art.web.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.art.web.compiler.config.converter.KryoHttpMessageConverter;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.model.Entity;
import org.art.web.compiler.service.CustomByteClassLoader;
import org.art.web.compiler.service.ServiceCommonConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.art.web.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompilerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Compilation POST request: Simple empty class (expects - application/octet-stream)")
    void test0() throws Exception {
        String className = "TestClass1";
        String src = "class TestClass1{}";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("src", src)
                        .param("classname", className)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andReturn();
        byte[] binClassData = result.getResponse().getContentAsByteArray();
        Map<String, byte[]> classData = new HashMap<>();
        classData.put(className, binClassData);
        CustomByteClassLoader loader = new CustomByteClassLoader(classData);
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("Compilation GET request: Simple empty class (expects - application/octet-stream)")
    void test1() throws Exception {
        String className = "TestClass2";
        String src = "class TestClass2{}";
        MvcResult result = mockMvc.perform(
                get("/compile/src?src={src}&classname={classname}",
                        URLEncoder.encode(src, StandardCharsets.UTF_8.name()),
                        URLEncoder.encode(className, StandardCharsets.UTF_8.name()))
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andReturn();
        byte[] binClassData = result.getResponse().getContentAsByteArray();
        Map<String, byte[]> classData = new HashMap<>();
        classData.put(className, binClassData);
        CustomByteClassLoader loader = new CustomByteClassLoader(classData);
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Disabled
    @Test
    @DisplayName("Compilation request: Simple empty class (expects - application/x-kryo)")
    void test2() throws Exception {
        String className = "TestClass3";
        String src = "class TestClass3{}";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("src", src)
                        .param("classname", className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andReturn();
        byte[] binData = result.getResponse().getContentAsByteArray();
        Map<String, byte[]> data = new HashMap<>();
        data.put(className, binData);
        CustomByteClassLoader loader = new CustomByteClassLoader(data);
        Class<?> clazz = loader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(className, clazz.getSimpleName());
    }

    @Test
    @DisplayName("Compilation POST request: Simple empty class with comp error (expects - application/json)")
    void test3() throws Exception {
        String className = "TestClass4";
        String src = "class TestClass4{}";
        MvcResult result = mockMvc.perform(
                post("/compile/src")
                        .param("src", src)
                        .param("classname", className)
                        .accept(KRYO_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", KRYO_CONTENT_TYPE))
                .andExpect(content().contentType(KRYO_CONTENT_TYPE))
                .andDo(print())
                .andReturn();
        byte[] binResponse = result.getResponse().getContentAsByteArray();
        System.out.println(Arrays.toString(binResponse));
        String str = result.getResponse().getContentAsString();
        assertNotNull(binResponse);
        System.out.println("!!!Resp: " + str);
        Kryo kryo = new Kryo();
        kryo.register(Entity.class, 10);
        Object responseDto = kryo.readClassAndObject(new Input(binResponse));
//        Object responseDto = kryo.readClassAndObject(new Input(binResponse));
        System.out.println(responseDto);
    }
}
