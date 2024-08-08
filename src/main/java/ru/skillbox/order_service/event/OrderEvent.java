package ru.skillbox.order_service.event;

public record OrderEvent(String product, Integer quantity) {
}
