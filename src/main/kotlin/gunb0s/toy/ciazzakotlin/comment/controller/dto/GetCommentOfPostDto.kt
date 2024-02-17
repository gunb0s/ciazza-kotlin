package gunb0s.toy.ciazzakotlin.comment.controller.dto

import jakarta.validation.constraints.NotNull

class GetCommentOfPostDto(
    @NotNull
    val postId: Long,
)