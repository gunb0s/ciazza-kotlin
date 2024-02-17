package gunb0s.toy.ciazzakotlin.post.controller.dto

import gunb0s.toy.ciazzakotlin.post.entity.Post
import io.swagger.v3.oas.annotations.media.Schema

class PostDto(
    @Schema(hidden = true)
    post: Post,
) {
    val id: Long = post.id!!
    val title: String = post.title
    val content: String = post.content
    val userId: Long = post.user.id!!
    val username: String = post.user.name
}