package gunb0s.toy.ciazzakotlin.board.service

import gunb0s.toy.ciazzakotlin.board.controller.dto.CreateBoardDto
import gunb0s.toy.ciazzakotlin.board.entity.Board
import gunb0s.toy.ciazzakotlin.board.repository.BoardRepository
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureRepository
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.Optional

class BoardServiceTest : DescribeSpec({
    val boardRepository = mockk<BoardRepository>()
    val educatorRepository = mockk<EducatorRepository>()
    val lectureRepository = mockk<LectureRepository>()
    val boardService = BoardService(boardRepository, educatorRepository, lectureRepository)

    describe("create") {
        context("createBoardDto 가 주어졌을 때") {
            val createBoardDto = CreateBoardDto(
                title = "title",
                educatorId = 1L,
                lectureId = 1L
            )
            it("educatorId 에 해당하는 Educator 가 존재하지 않으면 NoSuchElementException 을 던진다") {
                // given
                every { educatorRepository.existsById(createBoardDto.educatorId) } returns false

                // when
                // then
                shouldThrow<NoSuchElementException> {
                    boardService.create(createBoardDto)
                }
            }
            it("lectureId 에 해당하는 Lecture 가 존재하지 않으면 NoSuchElementException 을 던진다") {
                // given
                every { educatorRepository.existsById(createBoardDto.educatorId) } returns true
                every { lectureRepository.findById(createBoardDto.lectureId) } returns Optional.empty()

                // when, then
                shouldThrow<NoSuchElementException> {
                    boardService.create(createBoardDto)
                }
            }

            it("dto 로 받은 educatorId 와 조회한 lecture 의 educatorId 가 일치하지 않으면 IllegalArgumentException 을 던진다") {
                // given
                val lecture = mockk<Lecture>()
                val educator = mockk<Educator>()
                every { educator.id } returns 2L
                every { lecture.educator } returns educator
                every { educatorRepository.existsById(createBoardDto.educatorId) } returns true
                every { lectureRepository.findById(createBoardDto.lectureId) } returns Optional.of(lecture)

                // when, then
                shouldThrow<IllegalArgumentException> {
                    boardService.create(createBoardDto)
                }
            }

            it("모든 검증을 통과하면 board 를 생성하고 반환한다") {
                // given
                val lecture = mockk<Lecture>()
                val educator = mockk<Educator>()
                every { educator.id } returns 1L
                every { lecture.educator } returns educator
                every { educatorRepository.existsById(createBoardDto.educatorId) } returns true
                every { lectureRepository.findById(createBoardDto.lectureId) } returns Optional.of(lecture)
                every { boardRepository.save(any()) } returns mockk()

                // when
                val board = boardService.create(createBoardDto)

                // then
                board.title shouldBe createBoardDto.title
                board.lecture shouldBe lecture
            }
        }
    }
    describe("get") {
        context("boardId 가 주어졌을 때") {
            val boardId = 1L
            it("boardId 에 해당하는 Board 가 존재하지 않으면 NoSuchElementException 을 던진다") {
                // given
                every { boardRepository.findById(boardId) } returns Optional.empty()

                // when, then
                shouldThrow<NoSuchElementException> {
                    boardService.get(boardId)
                }
            }
            it("boardId 에 해당하는 Board 가 존재하면 반환한다") {
                // given
                val board = mockk<Board>()
                every { boardRepository.findById(boardId) } returns Optional.of(board)

                // when
                val result = boardService.get(boardId)

                // then
                result shouldBe board
            }
        }
    }
})