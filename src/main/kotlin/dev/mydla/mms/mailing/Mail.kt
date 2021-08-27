package dev.mydla.mms.mailing

internal data class Mail(
    var replyTo: String,
    var subject: String,
    var text: String
)
