package org.art.web.warrior.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName(HOME_VIEW_NAME);
        registry.addViewController("/home").setViewName(HOME_VIEW_NAME);
        registry.addViewController("/admin").setViewName(ADMIN_VIEW_NAME);
    }
}
