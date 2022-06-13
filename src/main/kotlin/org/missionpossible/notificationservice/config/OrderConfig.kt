package org.missionpossible.notificationservice.config

import org.apache.kafka.common.serialization.StringDeserializer
import org.missionpossible.notificationservice.consumers.Order
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class OrderConfig {

    @Bean
    fun orderConsumerFactory(properties: KafkaProperties) =
        DefaultKafkaConsumerFactory(
            properties.buildConsumerProperties(),
            StringDeserializer(),
            JsonDeserializer(Order::class.java)
        )

    @Bean
    fun orderContainerFactory(
        orderConsumerFactory: DefaultKafkaConsumerFactory<String, Order>
    ) = ConcurrentKafkaListenerContainerFactory<String, Order>()
        .apply {
            this.consumerFactory = orderConsumerFactory
        }
}