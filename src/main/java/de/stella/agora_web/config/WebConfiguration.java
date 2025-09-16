package de.stella.agora_web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the /images/ URL to the temp_images/ folder
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///d:/GITHUB/ÁGORA/Proyecto Personal/agora_back/temp_images/");
    }
}
