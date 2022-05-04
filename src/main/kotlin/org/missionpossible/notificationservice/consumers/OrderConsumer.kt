package org.missionpossible.notificationservice.consumers

import org.missionpossible.notificationservice.mailing.MailingDetails
import org.missionpossible.notificationservice.mailing.MailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderConsumer(@Autowired val mailSender: MailSender) {

    @KafkaListener(topics = ["#{'\${order.topic.name}'.split(',')}"], containerFactory = "orderKafkaListenerContainerFactory")
    fun consume(order: Order?) {
        val mailingDetails = MailingDetails(subject = "Greetings", to= "abc@xyz.com", body = "Hello! how are you")
        mailSender.sendMail(mailingDetails)
    }
}