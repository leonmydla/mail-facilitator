package dev.mydla.mms.mailing

internal data class MailDto(
    var replyTo: String,
    var subject: String,
    var message: String
)
