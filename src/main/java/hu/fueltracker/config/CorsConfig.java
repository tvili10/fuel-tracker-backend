package hu.fueltracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Allow localhost and common local network ranges (HTTP/HTTPS, any port)
                .allowedOriginPatterns(
                        "http://localhost:*",
                        "https://localhost:*",
                        "http://127.0.0.1:*",
                        "https://127.0.0.1:*",
                        "http://[::1]:*",
                        "https://[::1]:*",
                        "http://192.168.*:*",
                        "https://192.168.*:*",
                        "http://10.*:*",
                        "https://10.*:*",
                        "http://172.16.*:*",
                        "https://172.16.*:*",
                        "http://172.17.*:*",
                        "https://172.17.*:*",
                        "http://172.18.*:*",
                        "https://172.18.*:*",
                        "http://172.19.*:*",
                        "https://172.19.*:*",
                        "http://172.20.*:*",
                        "https://172.20.*:*",
                        "http://172.21.*:*",
                        "https://172.21.*:*",
                        "http://172.22.*:*",
                        "https://172.22.*:*",
                        "http://172.23.*:*",
                        "https://172.23.*:*",
                        "http://172.24.*:*",
                        "https://172.24.*:*",
                        "http://172.25.*:*",
                        "https://172.25.*:*",
                        "http://172.26.*:*",
                        "https://172.26.*:*",
                        "http://172.27.*:*",
                        "https://172.27.*:*",
                        "http://172.28.*:*",
                        "https://172.28.*:*",
                        "http://172.29.*:*",
                        "https://172.29.*:*",
                        "http://172.30.*:*",
                        "https://172.30.*:*",
                        "http://172.31.*:*",
                        "https://172.31.*:*",
                        "https://unique-biscuit-d4e197.netlify.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
