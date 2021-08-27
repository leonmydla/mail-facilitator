package dev.mydla.mms.mailing

import dev.mydla.mms.api.ResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [MailingController::class])
internal class MailingControllerAdvice {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    internal fun illegalArgumentHandler(exception: IllegalArgumentException): ResponseDto {
        return ResponseDto(exception.message.orEmpty())
    }
}
