package org.missionpossible.notificationservice.mailing

import org.springframework.core.io.InputStreamSource

data class MailingDetails(
    val subject: String,
    val to: String,
    val cc: String? = null,
    val body: String,
    val attachment: Pair<String, InputStreamSource>? = null
)