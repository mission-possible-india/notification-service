package org.missionpossible.notificationservice.mailing

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.missionpossible.notificationservice.consumers.Order
import org.springframework.core.io.ClassPathResource
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

class MailTest {

    private val mockTemplateEngine = mockk<TemplateEngine>() {
        every { process(any<String>(), any()) } returns "This is a test mail"
    }

    @Test
    fun `should prepare mail`() {
        val order = Order(emailId = "def@xyz.com", name = "Test", regId = "1001", amount = 100.0)
        val mail = MailStub(mockTemplateEngine)

        val preparedMail = mail.prepareMail(order)

        preparedMail.to shouldBe "def@xyz.com"
        preparedMail.cc shouldBe "abc@xyz.com"
        preparedMail.subject shouldBe "Test Mail Stub!"
        preparedMail.body shouldBe "This is a test mail"
        preparedMail.attachment?.first shouldBe "test-attachment"
        preparedMail.attachment?.second is ClassPathResource
    }

    @Test
    fun `should prepare mail with to`() {
        val order = Order(emailId = "def@xyz.com", name = "Test", regId = "1001", amount = 100.0)
        val mail = MailStub(mockTemplateEngine)

        val preparedMail = mail.prepareMail(order)

        preparedMail.to shouldBe "def@xyz.com"
    }

    @Test
    fun `should prepare mail with cc`() {
        val order = Order(emailId = "def@xyz.com", name = "Test", regId = "1001", amount = 100.0)
        val mail = MailStub(mockTemplateEngine)

        val preparedMail = mail.prepareMail(order)

        preparedMail.cc shouldBe "abc@xyz.com"
    }

    @Test
    fun `should prepare mail with subject`() {
        val order = Order(emailId = "def@xyz.com", name = "Test", regId = "1001", amount = 100.0)
        val mail = MailStub(mockTemplateEngine)

        val preparedMail = mail.prepareMail(order)

        preparedMail.subject shouldBe "Test Mail Stub!"
    }

    @Test
    fun `should prepare mail with body`() {
        val order = Order(emailId = "def@xyz.com", name = "Test", regId = "1001", amount = 100.0)
        val mail = MailStub(mockTemplateEngine)

        val preparedMail = mail.prepareMail(order)

        preparedMail.body shouldBe "This is a test mail"
    }

    @Test
    fun `should prepare mail with attachment`() {
        val order = Order(emailId = "def@xyz.com", name = "Test", regId = "1001", amount = 100.0)
        val mail = MailStub(mockTemplateEngine)

        val preparedMail = mail.prepareMail(order)

        preparedMail.attachment?.first shouldBe "test-attachment"
        preparedMail.attachment?.second is ClassPathResource
    }
}

class MailStub(templateEngine: TemplateEngine) : Mail(templateEngine) {
    override fun prepareContext(order: Order): Context {
        val ctx = Context()
        ctx.setVariable("name", order.name)

        return ctx
    }

    override fun getTemplateLocation() = "test"

    override fun getSubject() = "Test Mail Stub!"

    override fun getCC() = "abc@xyz.com"

    override fun getAttachment() = Pair("test-attachment", mockk<ClassPathResource>())

}
