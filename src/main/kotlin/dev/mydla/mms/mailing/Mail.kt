package dev.mydla.mms.mailing

internal data class Mail(
  var subject: String,
  var text: String,
  var replyTo: String
)
