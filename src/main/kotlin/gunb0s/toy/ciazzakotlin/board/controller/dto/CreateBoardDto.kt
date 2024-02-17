package gunb0s.toy.ciazzakotlin.board.controller.dto

import jakarta.validation.constraints.NotNull

class CreateBoardDto(
    @NotNull
    val title: String,
    @NotNull
    val lectureId: Long,
    @NotNull
    val educatorId: Long,
)