package dev.mydla.mms.mailing

import dev.mydla.mms.api.ResponseDto
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
internal class MailingController(
    private val service: MailingService
) {

    @PostMapping("/send")
    fun send(
        @RequestBody
        mailDto: MailDto
    ): ResponseDto {
        with(mailDto) {
            if (replyTo.isEmpty()) {
                throw IllegalArgumentException("replyTo can not be empty")
            }

            if (!EmailValidator.getInstance().isValid(replyTo)) {
                throw IllegalArgumentException("replyTo is not a valid email")
            }

            if (subject.isEmpty()) {
                throw IllegalArgumentException("subject can not be empty")
            }

            if (message.isEmpty()) {
                throw IllegalArgumentException("message can not be empty")
            }

            val mail = MailVo(replyTo, subject, message)
            service.send(mail)
        }

        return ResponseDto("")
    }
}
