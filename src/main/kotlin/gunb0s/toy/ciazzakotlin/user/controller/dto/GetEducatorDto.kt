package gunb0s.toy.ciazzakotlin.user.controller.dto

import gunb0s.toy.ciazzakotlin.user.entity.Educator
import io.swagger.v3.oas.annotations.media.Schema

class GetEducatorDto(
    @Schema(hidden = true)
    educator: Educator,
) {
    val id: Long = educator.id!!
    val name: String = educator.name
}
