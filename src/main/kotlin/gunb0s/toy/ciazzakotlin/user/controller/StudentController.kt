package gunb0s.toy.ciazzakotlin.user.controller

import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorResponseDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentResponseDto
import gunb0s.toy.ciazzakotlin.user.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "student", description = "the student API")
class StudentController(
    private val studentService: StudentService,
) {
    @Operation(summary = "Add student", description = "add a new student to system", tags = ["student"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CreateEducatorResponseDto::class)
                )
            ],
            description = "Successful operation",
        )]
    )
    @PostMapping("/student")
    fun create(@RequestBody @Valid createEducatorDto: CreateStudentDto): ResponseEntity<ResponseDto<CreateStudentResponseDto>> {
        val id = studentService.create(createEducatorDto)
        val responseDto = ResponseDto.created(CreateStudentResponseDto(id))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto)
    }
}