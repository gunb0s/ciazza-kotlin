package gunb0s.toy.ciazzakotlin.user.controller.dto

import gunb0s.toy.ciazzakotlin.user.entity.Student

class GetStudentDto(
    student: Student,
) {
    val id: Long = student.id!!
    val name: String = student.name
}