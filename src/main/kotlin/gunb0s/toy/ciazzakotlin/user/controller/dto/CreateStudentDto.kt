package gunb0s.toy.ciazzakotlin.user.controller.dto

import jakarta.validation.constraints.NotNull

class CreateStudentDto(
    @NotNull val name: String,
)