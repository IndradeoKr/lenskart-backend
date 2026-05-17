package com.demo.shopwave.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
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

    // Inject database credentials dynamically from your application properties files
    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/Lenskart}")
    private String defaultDbUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:password}")
    private String defaultPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource dataSource() {
        // Fetch from environments (Render, Heroku, etc.)
        String dbUrl = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        // Fall back cleanly to the injected properties if environment variables are not set
        if (dbUrl == null || dbUrl.isEmpty()) {
            dbUrl = defaultDbUrl;
        }
        if (username == null || username.isEmpty()) {
            username = defaultUsername;
        }
        if (password == null || password.isEmpty()) {
            password = defaultPassword;
        }

        // Support cloud database connections (postgres:// or jdbc:postgresql:// with credentials inside URL)
        if (dbUrl.startsWith("postgres://") || dbUrl.startsWith("jdbc:postgresql://")) {
            try {
                String uriString = dbUrl.startsWith("jdbc:") ? dbUrl.substring(5) : dbUrl;
                java.net.URI uri = new java.net.URI(uriString);
                
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();
                String userInfo = uri.getUserInfo();

                if (userInfo != null && userInfo.contains(":")) {
                    String[] creds = userInfo.split(":");
                    username = creds[0];
                    password = creds[1];
                }

                // Construct clean standard JDBC URL
                dbUrl = "jdbc:postgresql://" + host + (port != -1 ? ":" + port : "") + path;
                
            } catch (Exception e) {
                System.err.println("Failed to parse cloud DB_URL: " + e.getMessage());
            }
        }

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
