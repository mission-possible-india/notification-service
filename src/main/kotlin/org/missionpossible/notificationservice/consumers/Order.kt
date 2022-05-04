package org.missionpossible.notificationservice.consumers

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(val emailId: String, val events: List<String> = listOf(), val amount: Double) {

    fun isDonation(): Boolean = events.isEmpty()
}