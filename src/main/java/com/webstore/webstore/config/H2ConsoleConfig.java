package com.webstore.webstore.config;

import jakarta.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Импортируем через рефлексию, так как класс может быть в другом пакете
import java.lang.reflect.Constructor;

@Configuration
public class H2ConsoleConfig {

    @Bean
    @SuppressWarnings("unchecked")
    public ServletRegistrationBean<Servlet> h2Console() {
        try {
            // Пробуем загрузить класс WebServlet из H2
            Class<?> webServletClass = Class.forName("org.h2.server.web.WebServlet");
            Constructor<?> constructor = webServletClass.getDeclaredConstructor();
            Servlet servlet = (Servlet) constructor.newInstance();

            ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(servlet);
            registrationBean.addUrlMappings("/h2-console/*");
            registrationBean.setLoadOnStartup(1);
            return registrationBean;
        } catch (Exception e) {
            // Если не нашли WebServlet, пробуем JakartaWebServlet
            try {
                Class<?> webServletClass = Class.forName("org.h2.server.web.JakartaWebServlet");
                Constructor<?> constructor = webServletClass.getDeclaredConstructor();
                Servlet servlet = (Servlet) constructor.newInstance();

                ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(servlet);
                registrationBean.addUrlMappings("/h2-console/*");
                registrationBean.setLoadOnStartup(1);
                return registrationBean;
            } catch (Exception ex) {
                throw new RuntimeException("Не удалось создать H2 Console servlet. Проверьте зависимость H2 в build.gradle", ex);
            }
        }
    }
}