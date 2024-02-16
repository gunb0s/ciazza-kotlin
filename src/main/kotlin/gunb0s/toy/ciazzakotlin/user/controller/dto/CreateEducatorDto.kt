package gunb0s.toy.ciazzakotlin.user.controller.dto

import jakarta.validation.constraints.NotNull

class CreateEducatorDto(
    @NotNull
    val name: String,
)
