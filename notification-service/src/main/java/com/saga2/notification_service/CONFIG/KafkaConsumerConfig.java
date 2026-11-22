package com.saga2.notification_service.CONFIG;

import com.saga2.notification_service.event.ClaimSubmittedEvent;
import com.saga2.notification_service.event.DealerStatusChangedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private <T> ConcurrentKafkaListenerContainerFactory<String, T>
    createFactory(Class<T> eventType) {

        Map<String, Object> properties = new HashMap<>();
        properties.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );
        properties.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        JacksonJsonDeserializer<T> deserializer =
                new JacksonJsonDeserializer<>(eventType);

        deserializer.ignoreTypeHeaders();

        ConsumerFactory<String, T> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        properties,
                        new StringDeserializer(),
                        deserializer
                );

        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String, ClaimSubmittedEvent> claimKafkaListenerFactory() {

        return createFactory(ClaimSubmittedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String, DealerStatusChangedEvent> dealerKafkaListenerFactory() {

        return createFactory(DealerStatusChangedEvent.class);
    }
}
