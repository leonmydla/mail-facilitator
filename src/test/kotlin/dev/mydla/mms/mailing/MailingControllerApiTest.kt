package dev.mydla.mms.mailing

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.mydla.mms.api.ResponseDto
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(MailingController::class)
class MailingControllerApiTest(
    @Autowired
    val mockMvc: MockMvc,
    @Autowired
    val mapper: ObjectMapper
) {

    @MockkBean
    private lateinit var service: MailingService

    private val sendUrl = "/send"
    private val replyRecipient = "replyRecipient@mail.com"
    private val subject = "This is an interesting subject"
    private val message = "A message rich of interesting information"

    @Test
    fun `should send mail with valid data`() {
        val expectedMessage = ResponseDto("")
        every { service.send(any()) } answers { nothing }

        mockMvc.post(sendUrl) {
            contentType = APPLICATION_JSON
            content = createSendContent(replyRecipient, subject, message)
            accept = APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectedMessage)) }
        }
    }

    @Test
    fun `should not accept empty recipient`() {
        val expectedMessage = ResponseDto("replyTo can not be empty")

        mockMvc.post(sendUrl) {
            contentType = APPLICATION_JSON
            content = createSendContent("", subject, message)
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
            content { contentType(APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectedMessage)) }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["recipient", "recipient@tld", "recipient@.de", "recipient@domain.", "domain.com", "@domain.com", "recipient@invalid.tld"])
    fun `should not accept invalid email`(invalidEmail: String) {
        val expectedMessage = ResponseDto("replyTo is not a valid email")

        mockMvc.post(sendUrl) {
            contentType = APPLICATION_JSON
            content = createSendContent(invalidEmail, subject, message)
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
            content { contentType(APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectedMessage)) }
        }
    }

    @Test
    fun `should not accept empty subject`() {
        val expectedMessage = ResponseDto("subject can not be empty")

        mockMvc.post(sendUrl) {
            contentType = APPLICATION_JSON
            content = createSendContent(replyRecipient, "", message)
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
            content { contentType(APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectedMessage)) }
        }
    }

    @Test
    fun `should not accept empty message`() {
        val expectedMessage = ResponseDto("message can not be empty")

        mockMvc.post(sendUrl) {
            contentType = APPLICATION_JSON
            content = createSendContent(replyRecipient, subject, "")
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
            content { contentType(APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectedMessage)) }
        }
    }

    private fun createSendContent(replyRecipient: String, subject: String, message: String): String {
        val mail = MailVo(replyRecipient, subject, message)
        return mapper.writeValueAsString(mail)
    }

}
