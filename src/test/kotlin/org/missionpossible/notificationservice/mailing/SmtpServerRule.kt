package org.missionpossible.notificationservice.mailing

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import org.junit.rules.ExternalResource
import javax.mail.internet.MimeMessage

class SmtpServerRule(
    private val port: Int,
    private val smtpServer: GreenMail = GreenMail(ServerSetup(port, null, "smtp"))
) : ExternalResource() {

    override fun before() {
        super.before()
        smtpServer.start()
    }

    fun getMessages(): Array<MimeMessage?>? {
        return smtpServer.receivedMessages
    }

    override fun after() {
        super.after()
        smtpServer.stop()
    }
}