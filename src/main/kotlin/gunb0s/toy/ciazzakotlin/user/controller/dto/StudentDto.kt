package gunb0s.toy.ciazzakotlin.user.controller.dto

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.user.entity.Student
import io.swagger.v3.oas.annotations.media.Schema

class StudentDto(
    @Schema(hidden = true)
    student: Student,
) {
    val id: Long = student.id!!
    val name: String = student.name
    val enrollment: List<EnrollmentDto> = student
        .enrollments
        .map(::EnrollmentDto)

    class EnrollmentDto(
        @Schema(hidden = true)
        enrollment: Enrollment,
    ) {
        val id: Long = enrollment.id!!
        val lectureName: String = enrollment.lecture!!.name
        val educatorName: String = enrollment.lecture!!.educator!!.name
    }
}