package gunb0s.toy.ciazzakotlin.board.controller.dto

import gunb0s.toy.ciazzakotlin.board.entity.Board

class BoardDto(
    board: Board,
) {
    val id: Long = board.id!!
    val title: String = board.title
}