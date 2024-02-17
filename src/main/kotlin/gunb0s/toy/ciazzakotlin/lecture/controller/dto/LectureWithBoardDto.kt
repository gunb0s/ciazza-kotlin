package gunb0s.toy.ciazzakotlin.lecture.controller.dto

import gunb0s.toy.ciazzakotlin.board.entity.Board
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import io.swagger.v3.oas.annotations.media.Schema

class LectureWithBoardDto(
    @Schema(hidden = true)
    lecture: Lecture,
) {
    val id: Long = lecture.id!!
    val name = lecture.name
    val lectureCode = lecture.lectureCode
    val educatorId = lecture.educator.id!!
    val educatorName = lecture.educator.name
    val semester = lecture.semester
    val boards = lecture.boards
        .stream()
        .map { board: Board -> BoardDto(board) }
        .toList()

    class BoardDto(
        @Schema(hidden = true)
        board: Board,
    ) {
        val id: Long = board.id!!
        val title = board.title
    }
}
