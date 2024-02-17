package gunb0s.toy.ciazzakotlin.lecture.controller.dto

import gunb0s.toy.ciazzakotlin.lecture.entity.Semester
import jakarta.validation.constraints.NotNull

class CreateLectureDto(
    @NotNull
    val name: String,
    @NotNull
    val lectureCode: String,
    @NotNull
    val educatorId: Long,
    @NotNull
    val semester: Semester,
)