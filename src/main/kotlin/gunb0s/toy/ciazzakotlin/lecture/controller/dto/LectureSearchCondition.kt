package gunb0s.toy.ciazzakotlin.lecture.controller.dto

import gunb0s.toy.ciazzakotlin.lecture.entity.Semester

class LectureSearchCondition(
    val semester: Semester? = null,
    val lectureCode: String? = null,
    val educatorId: Long? = null,
)