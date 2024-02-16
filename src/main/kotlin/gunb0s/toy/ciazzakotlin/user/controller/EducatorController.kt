package gunb0s.toy.ciazzakotlin.user.controller

import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorResponseDto
import gunb0s.toy.ciazzakotlin.user.service.EducatorService
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
    fun create(@RequestBody @Valid createEducatorDto: CreateEducatorDto): ResponseEntity<CreateEducatorResponseDto> {
        val id = educatorService.create(createEducatorDto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CreateEducatorResponseDto(id))
    }
}