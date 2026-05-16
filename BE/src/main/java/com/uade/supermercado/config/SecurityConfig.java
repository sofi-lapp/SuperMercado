package com.uade.supermercado.config;

import com.uade.supermercado.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()

                // Rutas solo CLIENTE
                .requestMatchers(HttpMethod.GET, "/api/carrito/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/carrito/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.PUT, "/api/carrito/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/carrito/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/checkout/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/pedidos/mis-pedidos").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/notificaciones/**").hasRole("CLIENTE")

                // Rutas solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/pedidos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/categorias").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN")

                // Detalle de pedido: cliente ve el suyo admin ve cualquiera
                .requestMatchers(HttpMethod.GET, "/api/pedidos/**").authenticated()

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
