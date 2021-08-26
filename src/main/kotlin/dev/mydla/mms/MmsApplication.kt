package dev.mydla.mms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class MmsApplication

fun main(args: Array<String>) {
    runApplication<MmsApplication>(*args)
}
