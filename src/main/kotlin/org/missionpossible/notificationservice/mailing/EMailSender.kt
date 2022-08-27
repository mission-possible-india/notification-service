package org.missionpossible.notificationservice.mailing

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import javax.mail.internet.MimeMessage

@Component
class EMailSender(@Autowired val sender: JavaMailSender) {

    private val logger = LoggerFactory.getLogger(EMailSender::class.java)

    fun sendMail(mailingDetails: MailingDetails) {
        val mimeMessage: MimeMessage = sender.createMimeMessage()

        val helper = MimeMessageHelper(mimeMessage, true)
        helper.setSubject(mailingDetails.subject)
        helper.setText(mailingDetails.body, true)
        helper.setTo(mailingDetails.to)
        if (mailingDetails.cc != null) {
            helper.setCc(mailingDetails.cc)
        }
        if (mailingDetails.attachment != null) {
            helper.addAttachment(mailingDetails.attachment.first, mailingDetails.attachment.second)
        }

        sender.send(mimeMessage)
        logger.info("Mail Sent successfully to " + mailingDetails.to)
    }
}