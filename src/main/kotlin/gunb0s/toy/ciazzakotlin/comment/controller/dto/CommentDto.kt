package gunb0s.toy.ciazzakotlin.comment.controller.dto

import gunb0s.toy.ciazzakotlin.comment.entity.Comment

class CommentDto(
    comment: Comment,
) {
    val id: Long = comment.id!!
    val content: String = comment.content
    val userId: Long = comment.user.id!!
    val username: String = comment.user.name
    val parentCommentId: Long? = comment.parentComment?.id
}
