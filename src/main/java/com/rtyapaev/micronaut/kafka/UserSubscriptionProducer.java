package com.rtyapaev.micronaut.kafka;

import com.rtyapaev.micronaut.model.dto.UserSubscriptionDto;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UserSubscriptionProducer {
    @Topic("user-subscription")
    void sendUserSubscription(UserSubscriptionDto userSubscriptionDto);
}
