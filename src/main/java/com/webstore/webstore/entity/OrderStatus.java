package com.webstore.webstore.entity;

public enum OrderStatus {
    PENDING,    // Ожидает обработки
    CONFIRMED,  // Подтвержден
    SHIPPED,    // Отправлен
    DELIVERED,  // Доставлен
    CANCELLED   // Отменен
}