package gunb0s.toy.ciazzakotlin.post.dto

import jakarta.validation.constraints.NotNull

class CreatePostDto(
    val title: @NotNull String,

    val content: @NotNull String,

    val boardId: @NotNull Long,

    val lectureId: @NotNull Long,

    val userId: @NotNull Long,
)