package gunb0s.toy.ciazzakotlin.user.controller.dto

import gunb0s.toy.ciazzakotlin.user.entity.Educator

class GetEducatorDto(
    educator: Educator,
) {
    val id: Long = educator.id!!
    val name: String = educator.name
}
