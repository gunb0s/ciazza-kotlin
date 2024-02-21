package gunb0s.toy.ciazzakotlin.post.service

import gunb0s.toy.ciazzakotlin.board.entity.Board
import gunb0s.toy.ciazzakotlin.board.repository.BoardRepository
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentRepository
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureRepository
import gunb0s.toy.ciazzakotlin.post.controller.dto.GetPostListDto
import gunb0s.toy.ciazzakotlin.post.controller.dto.PostSearchCondition
import gunb0s.toy.ciazzakotlin.post.dto.CreatePostDto
import gunb0s.toy.ciazzakotlin.post.entity.Post
import gunb0s.toy.ciazzakotlin.post.repository.PostQueryRepository
import gunb0s.toy.ciazzakotlin.post.repository.PostRepository
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

class PostServiceTest : DescribeSpec({
    val postRepository = mockk<PostRepository>()
    val postQueryRepository = mockk<PostQueryRepository>()
    val userRepository = mockk<UserRepository>()
    val lectureRepository = mockk<LectureRepository>()
    val enrollmentRepository = mockk<EnrollmentRepository>()
    val boardRepository = mockk<BoardRepository>()
    val postService = PostService(
        postRepository,
        postQueryRepository,
        userRepository,
        lectureRepository,
        enrollmentRepository,
        boardRepository
    )
    describe("create") {
        context("createPostDto 가 주어졌을 때") {
            val createPostDto = CreatePostDto(
                userId = 1,
                lectureId = 1,
                boardId = 1,
                title = "title",
                content = "content"
            )
            it("userId 에 해당하는 user 가 없을 때 NoSuchElementException 이 발생한다") {
                // given
                // when
                every { userRepository.findById(createPostDto.userId) } returns Optional.empty()
                // then
                shouldThrow<NoSuchElementException> {
                    postService.create(createPostDto)
                }
            }
            context("user 가 Educator 일 때") {
                it("user 가 lectureId 의 lecture 를 생성한 사람이 아닐 경우 NoSuchElementException 이 발생한다") {
                    // given
                    val user = mockk<Educator>()
                    every { user.dtype } returns "E"
                    every { userRepository.findById(createPostDto.userId) } returns Optional.of(user)
                    every { lectureRepository.findByIdAndEducator(createPostDto.lectureId, user) } returns null
                    // when
                    // then
                    shouldThrow<NoSuchElementException> {
                        postService.create(createPostDto)
                    }
                }
            }
            context("user 가 Student 일 때") {
                it("lectureId 에 해당하는 lecture 가 없을 경우 NoSuchElementException 이 발생한다") {
                    // given
                    val user = mockk<Student>()
                    every { user.dtype } returns "S"
                    every { lectureRepository.findById(createPostDto.lectureId) } returns Optional.empty()

                    // when, then
                    shouldThrow<NoSuchElementException> {
                        postService.create(createPostDto)
                    }
                }
                it("user 가 등록하지 않은 lecture 일 경우 NoSuchElementException 이 발생한다") {
                    // given
                    val user = mockk<Student>()
                    every { user.dtype } returns "S"
                    every { lectureRepository.findById(createPostDto.lectureId) } returns Optional.of(mockk())
                    every { enrollmentRepository.findByStudentAndLecture(user, any()) } returns null
                    // when, then
                    shouldThrow<NoSuchElementException> {
                        postService.create(createPostDto)
                    }
                }
            }
            it("boardId 에 해당하는 board 가 없을 경우 NoSuchElementException 이 발생한다") {
                // given
                val user = mockk<Educator>()
                every { user.dtype } returns "E"
                every { userRepository.findById(createPostDto.userId) } returns Optional.of(user)
                every {
                    lectureRepository.findByIdAndEducator(
                        createPostDto.lectureId,
                        user
                    )
                } returns mockk()
                every { boardRepository.findById(createPostDto.boardId) } returns Optional.empty()
                // when, then
                shouldThrow<NoSuchElementException> {
                    postService.create(createPostDto)
                }
            }
            it("boardId 에 해당하는 board 가 lectureId 의 board 가 아닐 경우 IllegalArgumentException 이 발생한다") {
                // given
                val user = mockk<Educator>()
                every { user.dtype } returns "E"
                every { userRepository.findById(createPostDto.userId) } returns Optional.of(user)
                every {
                    lectureRepository.findByIdAndEducator(
                        createPostDto.lectureId,
                        user
                    )
                } returns mockk()
                val board = mockk<Board>()
                every { board.lecture.id } returns 2L
                every { boardRepository.findById(createPostDto.boardId) } returns Optional.of(board)
                // when, then
                shouldThrow<IllegalArgumentException> {
                    postService.create(createPostDto)
                }
            }
            it("모든 조건을 만족할 경우 post 가 생성된다") {
                // given
                val user = mockk<Educator>()
                every { user.dtype } returns "E"
                every { userRepository.findById(createPostDto.userId) } returns Optional.of(user)
                every {
                    lectureRepository.findByIdAndEducator(
                        createPostDto.lectureId,
                        user
                    )
                } returns mockk()
                val board = mockk<Board>()
                every { board.lecture.id } returns 1L
                every { boardRepository.findById(createPostDto.boardId) } returns Optional.of(board)
                every {
                    postRepository.save(any())
                } returns mockk()

                // when
                val result = postService.create(createPostDto)

                // then
                result.title shouldBe createPostDto.title
                result.content shouldBe createPostDto.content
                result.board shouldBe board
                result.user shouldBe user
            }
        }
    }
    describe("get") {
        context("postId 가 주어졌을 때") {
            it("postId 에 해당하는 post 가 없을 경우 NoSuchElementException 이 발생한다") {
                // given
                val postId = 1L
                every { postRepository.findByIdWithUser(postId) } returns null
                // when, then
                shouldThrow<NoSuchElementException> {
                    postService.get(postId)
                }
            }
            it("postId 에 해당하는 post 가 있을 경우 post 가 반환된다") {
                // given
                val postId = 1L
                val post = mockk<Post>()
                every { postRepository.findByIdWithUser(postId) } returns post
                // when
                val result = postService.get(postId)
                // then
                result shouldBe post
            }
        }
    }
    describe("getList") {
        context("postListDto, postSearchCondition, pageable 이 주어졌을 때") {
            it("post 들을 조회하여 반환한다") {
                // given
                val postListDto = GetPostListDto(
                    lectureId = 1L,
                    boardId = 1L
                )
                val postSearchCondition = mockk<PostSearchCondition>()
                val pageable = mockk<Pageable>()
                val page = mockk<Page<Post>>()
                every {
                    postQueryRepository.search(
                        postListDto.lectureId,
                        postListDto.boardId,
                        postSearchCondition,
                        pageable
                    )
                } returns page
                // when
                val result = postService.getList(postListDto, postSearchCondition, pageable)
                // then
                result shouldBe page
            }
        }
    }
})




























