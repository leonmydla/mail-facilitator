package dev.mydla.mms.mailing

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("mailing")
internal class MailingConfig {

    lateinit var sender: String
    lateinit var recipient: String
}
