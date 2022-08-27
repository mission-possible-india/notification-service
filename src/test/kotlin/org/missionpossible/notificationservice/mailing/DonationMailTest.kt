package org.missionpossible.notificationservice.mailing

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.missionpossible.notificationservice.consumers.Order

class DonationMailTest {

    @Test
    fun `should return context`() {
        val donationMail = DonationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        val context = donationMail.prepareContext(
            Order(
                emailId = "abc@xyz.com",
                regId = "100001",
                name = "Test User",
                amount = 10.0
            )
        )

        context.getVariable("name") shouldBe "Test User"
        context.getVariable("regId") shouldBe "100001"
    }

    @Test
    fun `should return template location as donations`() {
        val donationMail = DonationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        donationMail.getTemplateLocation() shouldBe "donations"
    }

    @Test
    fun `should return subject correctly`() {
        val donationMail = DonationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        donationMail.getSubject() shouldBe "Greetings from Mission Possible!!!"
    }

    @Test
    fun `should return cc address correctly`() {
        val ccAddress = "abc@xyz.com"
        val donationMail = DonationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        donationMail.getCC() shouldBe ccAddress
    }

    @Test
    fun `should return attachment for donation`() {
        val donationMail = DonationMail(templateEngine = mockk(), ccAddress = "abc@xyz.com")

        donationMail.getAttachment().first shouldBe "poster.jpg"
    }
}