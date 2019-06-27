package org.art.web.warrior.client.config;

import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.exchandler.CustomRestTemplateErrorHandler;
import org.art.web.warrior.client.config.interceptor.RequestLogger;
import org.art.web.warrior.client.service.client.retrofit.UserServiceRetroClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Bean
    @Profile(PROFILE_RETROFIT)
    public UserServiceRetroClient userServiceRetroClient(Environment env) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceEndpointBase(env))
                .addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER))
                .client(httpClient.build())
                .build();
        return retrofit.create(UserServiceRetroClient.class);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .additionalInterceptors(requestLogger())
                .additionalMessageConverters(new KryoHttpMessageConverter())
                .errorHandler(new CustomRestTemplateErrorHandler())
                .build();
    }

    @Bean
    public RequestLogger requestLogger() {
        return new RequestLogger();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName(LAYOUT_VIEW_NAME);
        registry.addViewController("/home").setViewName(LAYOUT_VIEW_NAME);

        registry.addViewController("/admin").setViewName(ADMIN_VIEW_NAME);
    }

    private String getServiceEndpointBase(Environment env) {
        String userServiceHostName;
        String userServiceHostPort;
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && PROFILE_CONTAINER.equals(activeProfile)) {
            userServiceHostName = env.getProperty(USER_SERVICE_HOST_ENV_PROP_NAME);
            userServiceHostPort = env.getProperty(USER_SERVICE_PORT_ENV_PROP_NAME);
        } else {
            userServiceHostName = LOCALHOST;
            userServiceHostPort = USER_SERVICE_PORT_NO_PROFILE;
        }
        return "http://" + userServiceHostName + ":" + userServiceHostPort + "/";
    }
}
