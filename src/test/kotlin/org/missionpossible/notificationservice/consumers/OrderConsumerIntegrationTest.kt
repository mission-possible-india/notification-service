package org.missionpossible.notificationservice.consumers

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetup
import io.kotest.matchers.shouldBe
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.awaitility.Awaitility.await
import org.awaitility.Durations
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import javax.mail.internet.MimeMultipart


@ActiveProfiles(value = ["test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"], topics = ["order-topic"])
class OrderConsumerIntegrationTest(
    @Autowired private val embeddedKafkaBroker: EmbeddedKafkaBroker,
    @Value(value = "\${order.topic.name}") val topic: String,
) {

    private lateinit var greenMail: GreenMail
    private lateinit var smtpSetup: ServerSetup

    @BeforeEach
    fun setUp() {
        smtpSetup = ServerSetup(3025, "localhost", "smtp")
        greenMail = GreenMail(smtpSetup)
        greenMail.setUser("username", "secret")
        greenMail.start()
        greenMail.purgeEmailFromAllMailboxes()
    }

    @Test
    fun `should send mail of participation`() {
        val order = Order(
            emailId = "ramesh@gmail.com",
            name = "Ramesh",
            regId = "40001",
            events = listOf("Quiz"),
            amount = 100.0
        )

        val producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker.brokersAsString)
        val producerTest: Producer<String, Order> = KafkaProducer(producerProps, StringSerializer(), JsonSerializer())
        producerTest.send(ProducerRecord(topic, order))

        await().atMost(Durations.TEN_SECONDS).untilAsserted{
            val receivedMessages = greenMail.receivedMessages

            receivedMessages?.size shouldBe 2 // One to the TO email another to CC
            receivedMessages[0]?.subject shouldBe "Greetings from Mission Possible!!!"
            val toMp = receivedMessages[0].content as MimeMultipart
            GreenMailUtil.getBody(toMp.getBodyPart(0)).contains("participating and contributing")

            receivedMessages[1]?.subject shouldBe "Greetings from Mission Possible!!!"
            val ccMP = receivedMessages[1].content as MimeMultipart
            GreenMailUtil.getBody(ccMP.getBodyPart(0)).contains("participating and contributing")
        }
    }

    @Test
    fun `should send mail of donation`() {
        val order = Order(
            emailId = "suresh@gmail.com",
            name = "Suresh",
            regId = "40001",
            amount = 1000.0
        )

        val producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker.brokersAsString)
        val producerTest: Producer<String, Order> = KafkaProducer(producerProps, StringSerializer(), JsonSerializer())
        producerTest.send(ProducerRecord(topic, order))

        await().atMost(Durations.TEN_SECONDS).untilAsserted{
            val receivedMessages = greenMail.receivedMessages

            receivedMessages?.size shouldBe 2 // One to the TO email another to CC
            receivedMessages[0]?.subject shouldBe "Greetings from Mission Possible!!!"
            val toMp = receivedMessages[0].content as MimeMultipart
            GreenMailUtil.getBody(toMp.getBodyPart(0)).contains("donations")

            receivedMessages[1]?.subject shouldBe "Greetings from Mission Possible!!!"
            val ccMP = receivedMessages[1].content as MimeMultipart
            GreenMailUtil.getBody(ccMP.getBodyPart(0)).contains("donations")
        }
    }

    @AfterEach
    fun tearDown() {
        greenMail.stop()
    }
}