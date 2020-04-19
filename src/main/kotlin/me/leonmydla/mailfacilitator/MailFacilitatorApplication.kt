package me.leonmydla.mailfacilitator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class MailFacilitatorApplication

fun main(args: Array<String>) {
    runApplication<MailFacilitatorApplication>(*args)
}
