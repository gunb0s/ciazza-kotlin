package gunb0s.toy.ciazzakotlin.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(value = [NoSuchElementException::class])
    fun noSuchElementExceptionHandler(ex: NoSuchElementException): ResponseEntity<ErrorResponseDto> {
        this.logger.error(ex.message, ex)
        val errorResponseDto = ErrorResponseDto(
            status = HttpStatus.NOT_FOUND,
            message = ex.message ?: "No such element"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponseDto)
    }

    @ExceptionHandler(InvalidRegistrationCodeException::class)
    fun handleInvalidRegistrationCodeException(ex: InvalidRegistrationCodeException): ResponseEntity<ErrorResponseDto> {
        logger.error(ex.message, ex)

        val dto: ErrorResponseDto = ErrorResponseDto(
            status = HttpStatus.BAD_REQUEST,
            message = ex.message!!
        )
        return ResponseEntity
            .badRequest()
            .body(dto)
    }
}