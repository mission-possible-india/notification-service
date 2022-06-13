package org.missionpossible.notificationservice.consumers

import org.missionpossible.notificationservice.mailing.DonationMail
import org.missionpossible.notificationservice.mailing.EMailSender
import org.missionpossible.notificationservice.mailing.ParticipationMail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderConsumer(
    @Autowired private val eMailSender: EMailSender,
    @Autowired private val participationMail: ParticipationMail,
    @Autowired private val donationMail: DonationMail
) {

    @KafkaListener(topics = ["#{'\${order.topic.name}'.split(',')}"], containerFactory = "orderContainerFactory")
    fun consume(order: Order) {
        println("Order recieved as [$order]")
        val mail = when (order.isDonation()) {
            true -> donationMail.prepareMail(order)
            else -> participationMail.prepareMail(order)
        }

        eMailSender.sendMail(mail)
    }
}