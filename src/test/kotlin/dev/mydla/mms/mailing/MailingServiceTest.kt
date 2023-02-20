package dev.mydla.mms.mailing

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import jakarta.mail.Message
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage

@ExtendWith(MockKExtension::class)
internal class MailingServiceTest {

    @MockK
    private lateinit var config: MailingConfig

    private lateinit var jMailSender: JavaMailSender

    private lateinit var classUnderTest: MailingService

    private val sender = "sender@mail.com"
    private val recipient = "recipient@mail.com"
    private val subject = "This is a subject"
    private val content = "Here is some mail content"
    private val replyRecipient = "replyRecipient@mail.com"

    @BeforeEach
    fun setUp() {
        jMailSender = spyk<JavaMailSenderImpl>()
        classUnderTest = MailingService(config, jMailSender)
    }

    @Test
    fun `should create MimeMessage using the JavaMailSender`() {
        every { jMailSender.createMimeMessage() } answers { callOriginal() }

        captureMail()

        verify(exactly = 1) { jMailSender.createMimeMessage() }
    }

    @Test
    fun `should assign correct sender`() {
        assertThat(captureMail().from).containsExactly(InternetAddress(sender))
    }

    @Test
    fun `should assign correct recipient`() {
        assertThat(captureMail().getRecipients(Message.RecipientType.TO)).containsExactly(InternetAddress(recipient))
    }

    @Test
    fun `should not assign copy recipients`() {
        val mail = captureMail()

        assertThat(mail.getRecipients(Message.RecipientType.CC)).isNull()
        assertThat(mail.getRecipients(Message.RecipientType.BCC)).isNull()
    }

    @Test
    fun `should assign correct reply recipient`() {
        assertThat(captureMail().replyTo).containsExactly(InternetAddress(replyRecipient))
    }

    @Test
    fun `should assign correct subject`() {
        assertThat(captureMail().subject).isEqualTo(subject)
    }

    @Test
    fun `should assign correct content`() {
        assertThat(captureMail().content).isEqualTo(content)
    }

    @Test
    fun `should send MimeMessage created by the JavaMailSender`() {
        val expectedMessage = jMailSender.createMimeMessage()

        every { jMailSender.createMimeMessage() } answers { expectedMessage }

        assertThat(captureMail()).isEqualTo(expectedMessage)
    }

    @Test
    fun `should send message`() {
        captureMail()

        verify(exactly = 1) { jMailSender.send(any<MimeMessage>()) }
    }

    private fun captureMail(): MimeMessage {
        val message = slot<MimeMessage>()
        val mail = MailVo(replyRecipient, subject, content)

        every { config.sender } returns sender
        every { config.recipient } returns recipient
        every { jMailSender.send(capture(message)) } answers { nothing }

        classUnderTest.send(mail)

        assertThat(message.isCaptured).isTrue()

        return message.captured
    }
}
