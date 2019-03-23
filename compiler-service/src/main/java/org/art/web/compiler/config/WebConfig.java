package org.art.web.compiler.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.web.compiler.config.converter.KryoHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final Logger LOG = LogManager.getLogger(WebConfig.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        LOG.debug("Adding Jackson 2 Http Message Converter");
        converters.add(new MappingJackson2HttpMessageConverter());
        LOG.debug("Adding Kryo Http Message Converter");
        converters.add(new KryoHttpMessageConverter());
    }
}
