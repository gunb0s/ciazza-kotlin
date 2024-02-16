package gunb0s.toy.ciazzakotlin.user.controller

import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorResponseDto
import gunb0s.toy.ciazzakotlin.user.service.EducatorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EducatorController(
    private val educatorService: EducatorService,
) {
    @PostMapping("/educator")
    fun create(@RequestBody @Valid createEducatorDto: CreateEducatorDto): ResponseEntity<CreateEducatorResponseDto> {
        val id = educatorService.create(createEducatorDto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CreateEducatorResponseDto(id))
    }
}