package org.missionpossible.notificationservice.consumers

import io.mockk.*
import org.junit.jupiter.api.Test
import org.missionpossible.notificationservice.mailing.DonationMail
import org.missionpossible.notificationservice.mailing.EMailSender
import org.missionpossible.notificationservice.mailing.MailingDetails
import org.missionpossible.notificationservice.mailing.ParticipationMail
import org.springframework.core.io.ClassPathResource
import org.thymeleaf.TemplateEngine

class OrderConsumerTest {

    private val mailingDetailsSlot = slot<MailingDetails>()
    private val mailSender = mockk<EMailSender>() {
        every { sendMail((capture(mailingDetailsSlot))) } just Runs
    }
    private val templateEngine = mockk<TemplateEngine>() {
        every { process("participation", any()) } returns "This is a participation test mail for quiz"
        every { process("donations", any()) } returns "This is a donations test mail"
    }
    private val participationMail = ParticipationMail(templateEngine, ccAddress = "abc@xyz.com")
    private val donationMail = DonationMail(templateEngine, ccAddress = "abc@xyz.com")
    private val orderConsumer = OrderConsumer(participationMail = participationMail, donationMail = donationMail, eMailSender = mailSender)

    @Test
    fun `should call mailsender service when participation order is recieved`() {
        val order = Order(emailId = "df@xyz.com", name = "dsf", regId = "40001", events = listOf("quiz"), amount = 100.0)

        orderConsumer.consume(order)

        verify(exactly = 1) {
            mailSender.sendMail(
                mailingDetails = MailingDetails(
                    subject = "Greetings from Mission Possible!!!",
                    to = "df@xyz.com",
                    cc = "abc@xyz.com",
                    body = "This is a participation test mail for quiz",
                    attachment = Pair("poster.jpg", ClassPathResource("attachment.jpg"))
                )
            )
        }
    }

    @Test
    fun `should call mailsender service when donation order is recieved`() {
        val order = Order(emailId = "df@xyz.com", name = "dsf", regId = "40001", amount = 100.0)

        orderConsumer.consume(order)

        verify(exactly = 1) {
            mailSender.sendMail(
                mailingDetails = MailingDetails(
                    subject = "Greetings from Mission Possible!!!",
                    to = "df@xyz.com",
                    cc = "abc@xyz.com",
                    body = "This is a donations test mail",
                    attachment = Pair("poster.jpg", ClassPathResource("attachment.jpg"))
                )
            )
        }
    }
}