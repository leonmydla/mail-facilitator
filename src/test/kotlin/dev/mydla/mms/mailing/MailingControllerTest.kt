package dev.mydla.mms.mailing

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(MockKExtension::class)
class MailingControllerTest {

    @MockK
    private lateinit var service: MailingService

    @InjectMockKs
    private lateinit var classUndertest: MailingController

    private val replyRecipient = "replyRecipient@mail.com"
    private val subject = "This is an interesting subject"
    private val message = "A message rich of interesting information"

    @Test
    fun `should send mail via service`() {
        every { service.send(any()) } answers { nothing }

        classUndertest.send(replyRecipient, subject, message)

        verify(exactly = 1) { service.send(any()) }
    }

    @Test
    fun `should not accept empty recipient`() {
        val exception = assertThrows<IllegalArgumentException> { classUndertest.send("", subject, message) }
        assertThat(exception.message).isEqualTo("replyTo can not be empty")
    }

    @ParameterizedTest
    @ValueSource(strings = ["recipient", "recipient@tld", "recipient@.de", "recipient@domain.", "domain.com", "@domain.com", "recipient@invalid.tld"])
    fun `should not accept invalid email`(invalidEmail: String) {
        val exception = assertThrows<IllegalArgumentException> { classUndertest.send(invalidEmail, subject, message) }
        assertThat(exception.message).isEqualTo("replyTo is not a valid email")
    }

    @Test
    fun `should not accept empty subject`() {
        val exception = assertThrows<IllegalArgumentException> { classUndertest.send(replyRecipient, "", message) }
        assertThat(exception.message).isEqualTo("subject can not be empty")
    }

    @Test
    fun `should not accept empty message`() {
        val exception = assertThrows<IllegalArgumentException> { classUndertest.send(replyRecipient, subject, "") }
        assertThat(exception.message).isEqualTo("message can not be empty")
    }
}
