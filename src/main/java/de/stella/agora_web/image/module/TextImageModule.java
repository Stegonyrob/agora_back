package de.stella.agora_web.image.module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.stella.agora_web.image.service.ITextImageService;
import de.stella.agora_web.image.service.impl.TextImageServiceImpl;

@Configuration
public class TextImageModule {

    @Bean
    public ITextImageService textImageService() {
        return new TextImageServiceImpl();
    }
}
