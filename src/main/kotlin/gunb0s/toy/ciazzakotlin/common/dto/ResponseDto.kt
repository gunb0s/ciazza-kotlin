package gunb0s.toy.ciazzakotlin.common.dto

import org.springframework.http.HttpStatus

class ResponseDto<T> {
    companion object {
        fun <T> ok(data: T): ResponseDto<T> {
            val response = ResponseDto<T>(true, HttpStatus.OK, data)
            return response
        }

        fun <T> created(data: T): ResponseDto<T> {
            val response = ResponseDto<T>(true, HttpStatus.CREATED, data)
            return response
        }
    }

    var result: Boolean
        private set
    var status: HttpStatus
        private set
    var data: T
        private set

    private constructor(result: Boolean, status: HttpStatus, data: T) {
        this.result = result
        this.status = status
        this.data = data
    }
}