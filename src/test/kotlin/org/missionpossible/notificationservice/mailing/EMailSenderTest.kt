package org.missionpossible.notificationservice.mailing

import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.InternalPlatformDsl.toStr
import org.apache.commons.mail.util.MimeMessageParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.Message.RecipientType.CC
import javax.mail.Message.RecipientType.TO
import javax.mail.Session
import javax.mail.internet.MimeMessage

class EMailSenderTest {

    private val javaMailSender = mockk<JavaMailSender>()
    private val EMailSender = EMailSender(javaMailSender)
    private val mimeMessageSlot = slot<MimeMessage>()

    @BeforeEach
    fun setUp() {
        val nullSession = null as Session?
        val mimeMessage = MimeMessage(nullSession)
        every { javaMailSender.createMimeMessage() } returns mimeMessage
        every { javaMailSender.send(capture(mimeMessageSlot)) } just Runs
    }

    @Test
    fun `should send mail`() {
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender.sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
        mimeMessageSlot.captured.subject shouldBe "Greetings from Mission Possible"
        mimeMessageSlot.captured.getRecipients(TO).size shouldBe 1
        mimeMessageSlot.captured.getRecipients(TO)[0].toStr() shouldBe "abc@xyz.com"
        mimeMessageSlot.captured.getRecipients(CC).size shouldBe 1
        mimeMessageSlot.captured.getRecipients(CC)[0].toStr() shouldBe "mission.possible@gmail.com"
        MimeMessageParser(mimeMessageSlot.captured).parse().htmlContent shouldBe "Thank you for your contribution"
        MimeMessageParser(mimeMessageSlot.captured).parse().attachmentList.size shouldBe 1
        MimeMessageParser(mimeMessageSlot.captured).parse().attachmentList[0].name shouldBe "poster.jpg"
    }

    @Test
    fun `should send mail without attachment`() {
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution"
        )

        EMailSender.sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
        MimeMessageParser(mimeMessageSlot.captured).parse().attachmentList.size shouldBe 0
    }

    @Test
    fun `should send mail without cc`() {
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            body = "Thank you for your contribution"
        )

        EMailSender.sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
        mimeMessageSlot.captured.getRecipients(CC) shouldBe null
    }

}