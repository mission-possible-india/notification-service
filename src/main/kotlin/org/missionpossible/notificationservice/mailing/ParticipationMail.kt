package org.missionpossible.notificationservice.mailing

import org.missionpossible.notificationservice.consumers.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Component
class ParticipationMail(
    @Autowired private val templateEngine: TemplateEngine,
    @Value(value = "\${application.cc-address}") val ccAddress: String
) : Mail(templateEngine) {

    override fun prepareContext(order: Order): Context {
        val ctx = Context()

        ctx.setVariable("name", order.name)
        ctx.setVariable("regId", order.regId)
        ctx.setVariable("eventsName", order.events.joinToString(","))

        return ctx
    }

    override fun getTemplateLocation() = "participation"

    override fun getSubject() = "Greetings from Mission Possible!!!"

    override fun getCC() = ccAddress

    override fun getAttachment() = Pair("poster.jpg", ClassPathResource("attachment.jpg"))
}