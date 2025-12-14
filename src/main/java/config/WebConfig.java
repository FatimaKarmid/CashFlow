package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Erlaube Anfragen von deiner Frontend-Domain
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "https://your-frontend-domain.com") // Hier die URL deiner Frontend-App eintragen
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
