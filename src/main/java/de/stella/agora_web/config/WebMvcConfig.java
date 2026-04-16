package de.stella.agora_web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuración para servir imágenes desde temp_images
        registry.addResourceHandler("/temp_images/**")
                .addResourceLocations("file:D:/GITHUB/ÁGORA/Proyecto Personal/agora_back/temp_images/")
                .setCachePeriod(3600) // Cache for 1 hour
                .resourceChain(true);
    }
}
