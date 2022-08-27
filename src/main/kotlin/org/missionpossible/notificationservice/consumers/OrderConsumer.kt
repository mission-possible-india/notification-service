package org.missionpossible.notificationservice.consumers

import org.apache.logging.log4j.LogManager.getLogger
import org.missionpossible.notificationservice.mailing.DonationMail
import org.missionpossible.notificationservice.mailing.EMailSender
import org.missionpossible.notificationservice.mailing.ParticipationMail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderConsumer(
    @Autowired private val eMailSender: EMailSender,
    @Autowired private val participationMail: ParticipationMail,
    @Autowired private val donationMail: DonationMail
) {
    private val logger = LoggerFactory.getLogger(OrderConsumer::class.java)

    @KafkaListener(topics = ["#{'\${order.topic.name}'.split(',')}"], containerFactory = "orderContainerFactory")
    fun consume(order: Order) {
        logger.info("Order recieved as [$order]")
        val mail = when (order.isDonation()) {
            true -> donationMail.prepareMail(order)
            else -> participationMail.prepareMail(order)
        }

        eMailSender.sendMail(mail)
    }
}