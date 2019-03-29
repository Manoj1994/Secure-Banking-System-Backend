package com.bankingapp.security;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
//@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "app/css/**",
                "app/js/**", "app/js/", "/")
                .addResourceLocations(
                        "classpath:/static/app/css",
                        "classpath:/static/app/js",
                        "classpath:/static/views",
                        "classpath:/static/");
    }
}
