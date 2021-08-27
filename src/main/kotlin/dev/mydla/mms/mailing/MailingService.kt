package dev.mydla.mms.mailing

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import javax.mail.internet.MimeMessage

@Service
internal class MailingService(
    private val config: MailingConfig,
    private val jMailSender: JavaMailSender
) {

    fun send(mail: MailVo) {
        val mimeMessage = assembleMessage(mail)
        jMailSender.send(mimeMessage)
    }

    private fun assembleMessage(mail: MailVo): MimeMessage {
        val mimeMessage = jMailSender.createMimeMessage()
        val messageHelper = MimeMessageHelper(mimeMessage)

        with(messageHelper) {
            setFrom(config.sender)
            setTo(config.recipient)
            setReplyTo(mail.replyTo)
            setSubject(mail.subject)
            setText(mail.message)
        }

        return mimeMessage
    }
}
