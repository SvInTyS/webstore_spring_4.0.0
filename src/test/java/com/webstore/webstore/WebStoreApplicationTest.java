package com.webstore.webstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Используем тестовый профиль
class WebStoreApplicationTest {

    @Test
    void contextLoads() {
        // Просто проверяем, что Spring контекст загружается
    }
}