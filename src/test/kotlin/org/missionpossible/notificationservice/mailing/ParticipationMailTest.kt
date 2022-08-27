package org.missionpossible.notificationservice.mailing

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.missionpossible.notificationservice.consumers.Order
import org.thymeleaf.TemplateEngine

class ParticipationMailTest {

    @Test
    fun `should return context`() {
        val participationMail = ParticipationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        val context = participationMail.prepareContext(
            Order(
                emailId = "abc@xyz.com",
                regId = "100001",
                name = "Test User",
                amount = 10.0,
                events = listOf("Quiz", "Singing")
            )
        )

        context.getVariable("name") shouldBe "Test User"
        context.getVariable("regId") shouldBe "100001"
        context.getVariable("eventsName") shouldBe "Quiz,Singing"
    }

    @Test
    fun `should return template location as participation`() {
        val participationMail = ParticipationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        participationMail.getTemplateLocation() shouldBe "participation"
    }

    @Test
    fun `should return subject correctly`() {
        val participationMail = ParticipationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        participationMail.getSubject() shouldBe "Greetings from Mission Possible!!!"
    }

    @Test
    fun `should return cc address correctly`() {
        val ccAddress = "abc@xyz.com"
        val participationMail = ParticipationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        participationMail.getCC() shouldBe ccAddress
    }

    @Test
    fun `should return attachment for participation`() {
        val participationMail = ParticipationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        participationMail.getAttachment().first shouldBe "poster.jpg"
    }
}