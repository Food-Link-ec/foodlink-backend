package com.foodlink.infrastructure.config;

import com.foodlink.infrastructure.adapter.input.rest.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/health").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/auth/logout").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/lotes/historial-expirados").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/lotes/buscar").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/lotes", "/api/v1/lotes/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/actuator/health", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/comercios", "/api/v1/beneficiarios", "/api/v1/compradores").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/lotes").hasAnyRole("COMERCIO", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/retiros/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/redistribucion/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/redistribucion/confirmar-venta").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/redistribucion/confirmar-donacion").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/impacto/mi-impacto").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/impacto/mi-impacto/reporte").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/impacto/comercio/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/impacto/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/auth/me").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/comercios/mis-lotes").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/redistribucion/mis-reservas").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/ia/analizar-imagen").hasAnyRole("COMERCIO", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/ia/sugerir-publicacion").hasAnyRole("COMERCIO", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/valoraciones/mis-valoraciones").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/valoraciones/comercio/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/compradores/mis-estadisticas").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOriginPatterns(java.util.List.of("*"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        return username -> {
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException(
                    "Use /api/v1/auth/login para autenticarse");
        };
    }
}