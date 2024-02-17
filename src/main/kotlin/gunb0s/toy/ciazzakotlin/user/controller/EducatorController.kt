package gunb0s.toy.ciazzakotlin.user.controller

import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.common.exception.ErrorResponseDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorResponseDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.GetEducatorDto
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.service.EducatorService
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
@Tag(name = "educator", description = "the educator API")
class EducatorController(
    private val educatorService: EducatorService,
) {
    @Operation(summary = "Add educator", description = "add a new educator to system", tags = ["educator"])
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
    @PostMapping("/educator")
    fun create(@RequestBody @Valid createEducatorDto: CreateEducatorDto): ResponseEntity<ResponseDto<CreateEducatorResponseDto>> {
        val id = educatorService.create(createEducatorDto)
        val responseDto = ResponseDto.created(CreateEducatorResponseDto(id))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto)
    }

    @Operation(summary = "get educator", description = "get educator with pagination", tags = ["educator"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @GetMapping("/educator")
    fun getList(@ParameterObject pageable: Pageable): ResponseEntity<ResponseDto<Page<GetEducatorDto>>> {
        val educators: Page<Educator> = educatorService.getList(pageable)
        val responseDto: ResponseDto<Page<GetEducatorDto>> = ResponseDto.ok(
            educators.map {
                GetEducatorDto(it)
            }
        )
        return ResponseEntity
            .ok(responseDto)
    }

    @Operation(summary = "get educator detail", description = "get educator by id", tags = ["educator"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Educator not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @GetMapping("/educator/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<ResponseDto<GetEducatorDto>> {
        val educator: Educator = educatorService.get(id)
        val responseDto = ResponseDto.ok(GetEducatorDto(educator))
        return ResponseEntity
            .ok(responseDto)
    }
}