package gunb0s.toy.ciazzakotlin.user.controller

import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.common.exception.ErrorResponseDto
import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorResponseDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentResponseDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.GetStudentDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.LectureDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.StudentLectureSearchCondition
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.service.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @Operation(summary = "get student", description = "get student with pagination", tags = ["student"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @GetMapping("/student")
    fun getList(
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<ResponseDto<Page<GetStudentDto>>> {
        val students: Page<Student> = studentService.getList(pageable)
        val responseDto: ResponseDto<Page<GetStudentDto>> = ResponseDto.ok(
            students.map {
                GetStudentDto(it)
            }
        )
        return ResponseEntity
            .ok<ResponseDto<Page<GetStudentDto>>>(responseDto)
    }

    @Operation(summary = "get student detail", description = "get student by id", tags = ["student"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @GetMapping("/student/{id}")
    fun get(
        @PathVariable id: Long,
    ): ResponseEntity<ResponseDto<GetStudentDto>> {
        val student: Student = studentService.get(id)
        val responseDto = ResponseDto.ok(GetStudentDto(student))
        return ResponseEntity
            .ok(responseDto)
    }

    @Operation(
        summary = "get student lectures",
        description = "get student lectures with pagination",
        tags = ["student"]
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @GetMapping("/student/{id}/lectures")
    fun getLectures(
        @PathVariable id: Long,
        @ParameterObject studentLectureSearchCondition: StudentLectureSearchCondition,
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<ResponseDto<Page<LectureDto>>> {
        val lectures: Page<Enrollment> = studentService.getLectures(id, studentLectureSearchCondition, pageable)
        val responseDto: ResponseDto<Page<LectureDto>> =
            ResponseDto.ok(lectures.map { LectureDto(it) })
        return ResponseEntity
            .ok<ResponseDto<Page<LectureDto>>>(responseDto)
    }
}