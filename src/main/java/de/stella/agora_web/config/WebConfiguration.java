package de.stella.agora_web.config;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ MAPEAR RECURSOS ESTÁTICOS A UN PATH DIFERENTE QUE NO CONFLICTE CON LA API
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/images/", "classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));

        // ✅ AGREGAR TAMBIÉN MAPEO PARA IMÁGENES DIRECTAMENTE
        registry.addResourceHandler("/images/**")
                .addResourceLocations("/static/images/", "classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(30)));
    }

}
