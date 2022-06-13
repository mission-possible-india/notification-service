package org.missionpossible.notificationservice.consumers

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OrderTest {

    @Test
    fun `should return isDonation=true when events are notpresent`() {
        val order = Order(emailId = "abc@xyz.com", name="Test", regId = "1001", amount = 100.0)

        order.isDonation() shouldBe true
    }

    @Test
    fun `should return isDonation=false when events are present`() {
        val order = Order(emailId = "abc@xyz.com", name="Test", regId = "1001", events = listOf("singing", "quiz"), amount = 100.0)

        order.isDonation() shouldBe false
    }
}
