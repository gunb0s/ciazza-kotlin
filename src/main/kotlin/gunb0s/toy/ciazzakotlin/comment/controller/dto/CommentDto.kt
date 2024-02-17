package gunb0s.toy.ciazzakotlin.comment.controller.dto

import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import io.swagger.v3.oas.annotations.media.Schema

class CommentDto(
    @Schema(hidden = true)
    comment: Comment,
) {
    val id: Long = comment.id!!
    val content: String = comment.content
    val userId: Long = comment.user.id!!
    val username: String = comment.user.name
    val parentCommentId: Long? = comment.parentComment?.id
}
