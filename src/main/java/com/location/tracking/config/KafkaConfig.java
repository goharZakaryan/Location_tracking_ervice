package com.location.tracking.config;

import com.location.tracking.entity.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.topic.location-updates}")
    private String locationUpdatesTopic;

    @Bean
    public DefaultErrorHandler errorHandler() {
        // Define custom error handler behavior
        return new DefaultErrorHandler();
    }


    @KafkaListener(topics = "${kafka.topic.location-updates}", groupId = "location-tracking-group")
    public void consumeLocationUpdate(Location location) {
        // Handle the consumed location data
        System.out.println("Consumed location update: " + location);
    }
}