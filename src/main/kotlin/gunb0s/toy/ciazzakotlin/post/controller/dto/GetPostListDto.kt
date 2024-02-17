package gunb0s.toy.ciazzakotlin.post.controller.dto

import jakarta.validation.constraints.NotNull

class GetPostListDto(
    @NotNull
    val lectureId: Long,
    @NotNull
    val boardId: Long,
)