package ru.skillbox.order_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    @Value("${app.kafka.producer.topic}")
    private String producerTopic;

    @KafkaListener(
            topics = "${app.kafka.consumer.topic}",
            groupId = "${app.kafka.group-id}",
            containerFactory = "containerFactory"
    )
    public void listen(
            @Payload OrderEvent event,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp
    ) {
        log.info("Received message: {}", event);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        kafkaTemplate.send(producerTopic, key, new OrderStatusEvent("CREATED", Instant.now()));
    }

}
