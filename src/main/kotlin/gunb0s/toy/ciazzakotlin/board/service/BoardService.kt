package gunb0s.toy.ciazzakotlin.board.service

import gunb0s.toy.ciazzakotlin.board.controller.dto.CreateBoardDto
import gunb0s.toy.ciazzakotlin.board.entity.Board
import gunb0s.toy.ciazzakotlin.board.repository.BoardRepository
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureRepository
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BoardService(
    private val boardRepository: BoardRepository,
    private val educatorRepository: EducatorRepository,
    private val lectureRepository: LectureRepository,
) {
    @Transactional
    fun create(createBoardDto: CreateBoardDto): Board {
        val exists: Boolean = educatorRepository.existsById(createBoardDto.educatorId)
        if (!exists) {
            throw NoSuchElementException("educator not found with id: $createBoardDto.educatorId")
        }
        val lecture: Lecture = lectureRepository.findById(createBoardDto.lectureId).orElseThrow {
            NoSuchElementException(
                "lecture not found with id: $createBoardDto.lectureId"
            )
        }

        require(
            lecture.educator.id!! == createBoardDto.educatorId
        ) { "educator and lecture mismatched" }

        val board = Board(
            title = createBoardDto.title,
            lecture = lecture
        )

        boardRepository.save(board)
        return board
    }

    fun get(boardId: Long): Board {
        return boardRepository.findById(boardId)
            .orElseThrow { NoSuchElementException("board not found with id: $boardId") }
    }
}
