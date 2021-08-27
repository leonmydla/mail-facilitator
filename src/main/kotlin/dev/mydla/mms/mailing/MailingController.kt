package dev.mydla.mms.mailing

import org.apache.commons.validator.routines.EmailValidator
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
internal class MailingController(
    private val service: MailingService
) {

    fun send(replyTo: String, subject: String, message: String) {
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

        val mail = Mail(replyTo, subject, message)
        service.send(mail)
    }
}
