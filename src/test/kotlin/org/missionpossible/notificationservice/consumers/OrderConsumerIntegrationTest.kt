package org.missionpossible.notificationservice.consumers

import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class OrderConsumerIntegrationTest(
    @Autowired private var embeddedKafkaBroker: EmbeddedKafkaBroker,
    @Autowired private var orderConsumer: OrderConsumer,
    @Value(value = "\${order.topic.name}") val topic: String
) {

    private lateinit var producer: Producer<String, Order>

    @BeforeEach
    fun setUp() {
        val configs = KafkaTestUtils.producerProps(embeddedKafkaBroker)
        producer = DefaultKafkaProducerFactory<String, Order>(configs, StringSerializer(), JsonSerializer())
            .createProducer()
    }

    @Test
    fun `should decrease countdown latch`() {
        val record = ProducerRecord<String, Order>(topic, Order("df", listOf("fs"), 100.0))
        producer.send(record)
        producer.flush()

        verify(exactly = 1) { orderConsumer.consume(any()) }
    }
}