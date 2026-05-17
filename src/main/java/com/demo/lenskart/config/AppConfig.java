package com.demo.lenskart.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.sql.DataSource;

@Configuration
public class AppConfig {

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

        System.out.println("DEBUG: Processing DB_URL: " + dbUrl);

        if (dbUrl.startsWith("postgres://") || dbUrl.startsWith("jdbc:postgresql://")) {
            try {
                // Use java.net.URI to parse the string reliably
                String uriString = dbUrl.startsWith("jdbc:") ? dbUrl.substring(5) : dbUrl;
                java.net.URI uri = new java.net.URI(uriString);
                
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();
                String userInfo = uri.getUserInfo();

                if (userInfo != null && userInfo.contains(":")) {
                    String[] creds = userInfo.split(":");
                    if (username == null || username.isEmpty()) username = creds[0];
                    if (password == null || password.isEmpty()) password = creds[1];
                }

                // Construct a standard JDBC URL without user info
                dbUrl = "jdbc:postgresql://" + host + (port != -1 ? ":" + port : "") + path;
                
            } catch (Exception e) {
                System.err.println("DEBUG: Failed to parse DB_URL URI: " + e.getMessage());
            }
        }

        if (username == null) username = "postgres";
        if (password == null) password = "password";

        System.out.println("DEBUG: Final JDBC URL: " + dbUrl);
        System.out.println("DEBUG: Final Username: " + username);

        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            allowedOrigins = "http://localhost:5173";
        }

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
