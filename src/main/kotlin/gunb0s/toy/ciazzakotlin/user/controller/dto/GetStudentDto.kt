package gunb0s.toy.ciazzakotlin.user.controller.dto

import gunb0s.toy.ciazzakotlin.user.entity.Student
import io.swagger.v3.oas.annotations.media.Schema

class GetStudentDto(
    @Schema(hidden = true)
    student: Student,
) {
    val id: Long = student.id!!
    val name: String = student.name
}