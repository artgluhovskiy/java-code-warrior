package org.art.web.warrior.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/user/login").setViewName("login/login");
        registry.addViewController("/user/signin").setViewName("signin/signin");
        registry.addViewController("user/registration").setViewName("registration/registration");

        registry.addViewController("/admin").setViewName("admin/admin");
    }
}
