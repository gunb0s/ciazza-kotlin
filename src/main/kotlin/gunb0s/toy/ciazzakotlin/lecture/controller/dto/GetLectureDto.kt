package gunb0s.toy.ciazzakotlin.lecture.controller.dto

import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.entity.Semester
import io.swagger.v3.oas.annotations.media.Schema

class GetLectureDto(
    @Schema(hidden = true)
    lecture: Lecture,
) {
    val id: Long = lecture.id!!
    val name: String = lecture.name
    val lectureCode: String = lecture.lectureCode
    val educatorId: Long = lecture.educator.id!!
    val educatorName: String = lecture.educator.name
    val semester: Semester = lecture.semester
}
