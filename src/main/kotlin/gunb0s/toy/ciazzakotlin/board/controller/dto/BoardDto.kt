package gunb0s.toy.ciazzakotlin.board.controller.dto

import gunb0s.toy.ciazzakotlin.board.entity.Board
import io.swagger.v3.oas.annotations.media.Schema

class BoardDto(
    @Schema(hidden = true)
    board: Board,
) {
    val id: Long = board.id!!
    val title: String = board.title
}