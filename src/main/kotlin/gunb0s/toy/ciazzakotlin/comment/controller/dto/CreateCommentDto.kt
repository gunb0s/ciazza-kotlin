package gunb0s.toy.ciazzakotlin.comment.controller.dto

import jakarta.validation.constraints.NotNull

class CreateCommentDto(
    val content: @NotNull String,

    val postId: @NotNull Long,

    val userId: @NotNull Long,

    val parentCommentId: Long? = null,
) {
    fun isRootCommentRequest(): Boolean {
        return parentCommentId == null
    }
}
