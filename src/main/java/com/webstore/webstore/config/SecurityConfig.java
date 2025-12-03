package com.webstore.webstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // публичные страницы (каталог, товары, регистрация, статические ресурсы)
                        .requestMatchers("/", "/index", "/catalog", "/catalog/**", "/product/**",
                                "/login", "/register", "/css/**", "/js/**", "/images/**",
                                "/db-info", "/db-test").permitAll()
                        // H2 Console — доступно только локально via browser, но CSRF handled separately
                        .requestMatchers("/h2-console/**").permitAll()
                        // Админ-панель — только ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Заказы и пользовательские страницы — USER
                        .requestMatchers("/orders/**", "/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                // Разрешаем frame для H2 Console на том же origin
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // CSRF: игнорируем H2 Console (H2 POST формы не используют CSRF токен)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );

        return http.build();
    }

    // Password encoder bean (у вас уже был)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Явный AuthenticationManager — полезно и безопасно
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}