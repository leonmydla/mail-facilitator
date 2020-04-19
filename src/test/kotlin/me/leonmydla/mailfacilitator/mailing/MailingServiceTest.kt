package me.leonmydla.mailfacilitator.mailing

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@ExtendWith(MockKExtension::class)
internal class MailingServiceTest {

    @Suppress("unused")
    @MockK
    private lateinit var config: MailingConfig

    @RelaxedMockK
    private lateinit var jMailSender: JavaMailSender

    @InjectMockKs
    private lateinit var classUnderTest: MailingService

    @Test
    fun `should assemble mimeMessage correctly`() {
        val sender = "sender@mail.com"
        val recipient = "recipient@mail.com"
        val mail = Mail("subject", "text", "reply@to.com")
        val mimeMessage = slot<MimeMessage>()

        every { config.sender } returns sender
        every { config.recipient } returns recipient
        every { jMailSender.send(capture(mimeMessage)) } answers { nothing }

        classUnderTest.send(mail)

        assertThat(mimeMessage.isCaptured).isTrue()
        assertThat(mimeMessage.captured.sender).isEqualTo(sender)
        assertThat(mimeMessage.captured.getRecipients(Message.RecipientType.TO)).isEqualTo(InternetAddress(recipient))
        assertThat(mimeMessage.captured.getRecipients(Message.RecipientType.CC)).isNull()
        assertThat(mimeMessage.captured.getRecipients(Message.RecipientType.BCC)).isNull()
        assertThat(mimeMessage.captured.subject).isEqualTo(mail.subject)
        assertThat(mimeMessage.captured.content).isEqualTo(mail.text)
        assertThat(mimeMessage.captured.replyTo).isEqualTo(mail.replyTo)
    }
}
