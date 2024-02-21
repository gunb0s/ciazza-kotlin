package gunb0s.toy.ciazzakotlin.lecture.controller

import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.common.exception.ErrorResponseDto
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.*
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.service.LectureService
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
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "lecture", description = "the lecture API")
class LectureController(
    private val lectureService: LectureService,
) {
    @Operation(summary = "create lecture", description = "add a new lecture to system", tags = ["lecture"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @PostMapping("/lecture")
    fun create(@RequestBody @Valid createLectureDto: CreateLectureDto): ResponseEntity<ResponseDto<CreateLectureResponseDto>> {
        val lecture = lectureService.create(createLectureDto)
        val responseDto: ResponseDto<CreateLectureResponseDto> =
            ResponseDto.created(CreateLectureResponseDto(lecture.id!!))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body<ResponseDto<CreateLectureResponseDto>>(responseDto)
    }

    @Operation(summary = "enroll lecture", description = "enroll lecture with registration code", tags = ["lecture"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Entity not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        ), ApiResponse(
            responseCode = "400",
            description = "Invalid registration code",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @PostMapping("/lecture/{lectureId}")
    fun enroll(
        @PathVariable lectureId: Long,
        @RequestBody @Valid enrollLectureDto: EnrollLectureDto,
    ): ResponseEntity<ResponseDto<EnrollLectureResponseDto>> {
        val enrollment = lectureService.enroll(lectureId, enrollLectureDto)
        val responseDto: ResponseDto<EnrollLectureResponseDto> =
            ResponseDto.created(EnrollLectureResponseDto(enrollment.id!!))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto)
    }

    @Operation(summary = "get lecture", description = "get lecture with pagination", tags = ["lecture"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @GetMapping("/lecture")
    fun getList(
        @ParameterObject lectureSearchCondition: LectureSearchCondition,
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<ResponseDto<Page<GetLectureDto>>> {
        val lectures: Page<Lecture> = lectureService.getList(lectureSearchCondition, pageable)
        val responseDto: ResponseDto<Page<GetLectureDto>> = ResponseDto.ok(
            lectures.map {
                GetLectureDto(it)
            })
        return ResponseEntity
            .ok<ResponseDto<Page<GetLectureDto>>>(responseDto)
    }

    @Operation(summary = "get lecture detail", description = "get lecture by id", tags = ["lecture"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Lecture not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @GetMapping("/lecture/{lectureId}")
    fun get(@PathVariable lectureId: Long): ResponseEntity<ResponseDto<LectureWithBoardDto>> {
        val lecture: Lecture = lectureService.get(lectureId)
        val responseDto: ResponseDto<LectureWithBoardDto> = ResponseDto.ok(LectureWithBoardDto(lecture))
        return ResponseEntity
            .ok(responseDto)
    }
}