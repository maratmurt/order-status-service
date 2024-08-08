package ru.skillbox.order_service.event;

import java.time.Instant;

public record OrderStatusEvent(String status, Instant date) {
}
