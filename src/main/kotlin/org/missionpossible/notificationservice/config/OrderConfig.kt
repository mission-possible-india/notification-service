package org.missionpossible.notificationservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.common.serialization.StringDeserializer
import org.missionpossible.notificationservice.consumers.Order
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class OrderConfig(
    @Value(value = "\${kafka.bootstrap.address}") val bootstrapAddress: String,
    @Value(value = "\${kafka.consumer.group-id}") val group: String
) {

    @Bean
    fun orderConsumerFactory(): ConsumerFactory<String?, Order?> {
        val props: MutableMap<String, Any> = HashMap()
        props[BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[GROUP_ID_CONFIG] = group
        props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java

        return DefaultKafkaConsumerFactory<String?, Order?>(
            props,
            StringDeserializer(),
            JsonDeserializer(Order::class.java)
        );
    }

    @Bean
    fun orderKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Order>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Order>()
        factory.consumerFactory = orderConsumerFactory()

        return factory
    }
}