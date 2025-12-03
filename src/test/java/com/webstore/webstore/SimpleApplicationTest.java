package com.webstore.webstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,  // Используем mock веб-окружение
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
        }
)
class SimpleApplicationTest {

    @MockBean
    EnableWebSecurity enableWebSecurity;  // Мокаем Security для тестов

    @Test
    void contextLoads() {
        // Тест проверяет только загрузку контекста
    }
}