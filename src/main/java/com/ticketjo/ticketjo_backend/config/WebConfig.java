package com.ticketjo.ticketjo_backend.config;

import org.springframework.context.annotation.Configuration;import org.springframework.web.servlet.config.annotation.*;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  
    //  mappings MVC existants (static, uploads, etc.) ---
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = "file:" + System.getProperty("user.home") + "/uploads/";
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadDir);
    }
}
