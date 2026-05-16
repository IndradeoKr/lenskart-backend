package com.demo.lenskart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource dataSource() {
        String dbUrl = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        // Use defaults for local development
        if (dbUrl == null) {
            dbUrl = "jdbc:postgresql://localhost:5432/Lenskart";
        }

        // If it's a URI style (postgres://user:password@host:port/db)
        if (dbUrl.startsWith("postgres://") || dbUrl.startsWith("jdbc:postgresql://")) {
            try {
                // Remove jdbc: prefix if present for parsing
                String cleanUrl = dbUrl.replace("jdbc:postgresql://", "").replace("postgres://", "");
                
                // Format: user:password@host:port/db
                String[] firstSplit = cleanUrl.split("@");
                if (firstSplit.length == 2) {
                    // We have credentials in the URL
                    String credentials = firstSplit[0];
                    String hostAndDb = firstSplit[1];
                    
                    String[] creds = credentials.split(":");
                    if (username == null || username.isEmpty()) username = creds[0];
                    if (password == null || password.isEmpty()) password = creds[1];
                    
                    // Reconstruct a clean JDBC URL: jdbc:postgresql://host:port/db
                    dbUrl = "jdbc:postgresql://" + hostAndDb;
                } else {
                    // No credentials in URL, just ensure it starts with jdbc:postgresql://
                    dbUrl = "jdbc:postgresql://" + cleanUrl;
                }
            } catch (Exception e) {
                // Fallback to whatever was provided if parsing fails
            }
        }

        if (username == null) username = "postgres";
        if (password == null) password = "password";

        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            allowedOrigins = "http://localhost:5173"; // Default Vite port
        }
        
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);
    }
}
