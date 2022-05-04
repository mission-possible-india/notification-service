package org.missionpossible.notificationservice.consumers

import io.mockk.*
import org.junit.jupiter.api.Test
import org.missionpossible.notificationservice.mailing.MailSender

class OrderConsumerTest {

    private val mailSender = mockk<MailSender>() {
        every { sendMail(any()) } just Runs
    }
    private val orderConsumer  = OrderConsumer(mailSender)

    @Test
    fun `should call mailsender service when order is recieved`() {
        val order = Order(emailId = "abc@xyz.com", amount = 100.0)

        orderConsumer.consume(order)

        verify(exactly = 1) { mailSender.sendMail(any()) }
    }
}