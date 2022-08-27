package org.missionpossible.notificationservice.mailing

import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.InternalPlatformDsl.toStr
import org.apache.commons.mail.util.MimeMessageParser
import org.junit.jupiter.api.Test
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.Message.RecipientType.CC
import javax.mail.Message.RecipientType.TO
import javax.mail.Session
import javax.mail.internet.MimeMessage

class EMailSenderTest {

    @Test
    fun `should send mail exactly once`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
    }

    @Test
    fun `should send mail with subject`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
        mimeMessageSlot.captured.subject shouldBe "Greetings from Mission Possible"
    }

    @Test
    fun `should send mail with to`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        mimeMessageSlot.captured.getRecipients(TO).size shouldBe 1
        mimeMessageSlot.captured.getRecipients(TO)[0].toStr() shouldBe "abc@xyz.com"
    }

    @Test
    fun `should send mail with cc`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        mimeMessageSlot.captured.getRecipients(CC).size shouldBe 1
        mimeMessageSlot.captured.getRecipients(CC)[0].toStr() shouldBe "mission.possible@gmail.com"
    }

    @Test
    fun `should send mail with content`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        MimeMessageParser(mimeMessageSlot.captured).parse().htmlContent shouldBe "Thank you for your contribution"
    }

    @Test
    fun `should send mail with attachment`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution",
            attachment = Pair("poster.jpg", FileSystemResource("src/test/resources/attachment.jpg"))
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        MimeMessageParser(mimeMessageSlot.captured).parse().htmlContent shouldBe "Thank you for your contribution"
        MimeMessageParser(mimeMessageSlot.captured).parse().attachmentList.size shouldBe 1
        MimeMessageParser(mimeMessageSlot.captured).parse().attachmentList[0].name shouldBe "poster.jpg"
    }

    @Test
    fun `should send mail without attachment`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            cc = "mission.possible@gmail.com",
            body = "Thank you for your contribution"
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
        MimeMessageParser(mimeMessageSlot.captured).parse().attachmentList.size shouldBe 0
    }

    @Test
    fun `should send mail without cc`() {
        val mimeMessageSlot = slot<MimeMessage>()
        val javaMailSender = mockk<JavaMailSender>() {
            every { createMimeMessage() } returns MimeMessage(null as Session?)
            every { send(capture(mimeMessageSlot)) } just Runs
        }
        val mailingDetails = MailingDetails(
            subject = "Greetings from Mission Possible",
            to = "abc@xyz.com",
            body = "Thank you for your contribution"
        )

        EMailSender(javaMailSender).sendMail(mailingDetails)

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
        mimeMessageSlot.captured.getRecipients(CC) shouldBe null
    }

}