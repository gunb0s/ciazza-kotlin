package gunb0s.toy.ciazzakotlin.common.exception

import org.springframework.http.HttpStatus

class ErrorResponseDto(
    val status: HttpStatus,
    val message: String,
)