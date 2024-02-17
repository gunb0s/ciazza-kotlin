package gunb0s.toy.ciazzakotlin.user.controller.dto

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.lecture.entity.Semester
import io.swagger.v3.oas.annotations.media.Schema

class LectureDto(
    @Schema(hidden = true)
    enrollment: Enrollment,
) {
    val id: Long = enrollment.id!!
    val lectureId: Long = enrollment.lecture!!.id!!
    val lectureName: String = enrollment.lecture!!.name
    val semester: Semester = enrollment.lecture!!.semester
}
