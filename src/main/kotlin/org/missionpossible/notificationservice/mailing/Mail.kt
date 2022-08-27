package org.missionpossible.notificationservice.mailing

import org.missionpossible.notificationservice.consumers.Order
import org.springframework.core.io.InputStreamSource
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

abstract class Mail(private val templateEngine: TemplateEngine) {

    fun prepareMail(order: Order): MailingDetails {
        val context = prepareContext(order)
        val mailBody = templateEngine.process(getTemplateLocation(), context)

        return MailingDetails(
            subject = getSubject(),
            to = order.emailId,
            cc = getCC(),
            body = mailBody,
            attachment = getAttachment()
        )
    }

    abstract fun prepareContext(order: Order): Context

    abstract fun getTemplateLocation(): String

    abstract fun getSubject(): String

    abstract fun getCC(): String?

    abstract fun getAttachment(): Pair<String, InputStreamSource>?

}