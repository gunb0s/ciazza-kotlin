package gunb0s.toy.ciazzakotlin.lecture.controller.dto

import jakarta.validation.constraints.NotNull

class EnrollLectureDto(
    @NotNull
    val studentId: Long,

    @NotNull
    val registrationCode: String,
)