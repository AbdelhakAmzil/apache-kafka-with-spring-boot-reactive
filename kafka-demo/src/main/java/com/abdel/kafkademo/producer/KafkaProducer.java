package com.abdel.kafkademo.producer;

import com.abdel.kafkademo.payload.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        Message<String> msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, "abdel")
                .build();

        kafkaTemplate.send(msg);
    }
}
