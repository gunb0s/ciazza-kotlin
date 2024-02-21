package gunb0s.toy.ciazzakotlin.comment.service

import gunb0s.toy.ciazzakotlin.comment.controller.dto.CreateCommentDto
import gunb0s.toy.ciazzakotlin.comment.controller.dto.GetCommentOfPostDto
import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.comment.repository.CommentQueryRepository
import gunb0s.toy.ciazzakotlin.comment.repository.CommentRepository
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentRepository
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureRepository
import gunb0s.toy.ciazzakotlin.post.entity.Post
import gunb0s.toy.ciazzakotlin.post.repository.PostRepository
import gunb0s.toy.ciazzakotlin.user.entity.User
import gunb0s.toy.ciazzakotlin.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

class CommentServiceTest : DescribeSpec({
    val commentRepository = mockk<CommentRepository>()
    val commentQueryRepository = mockk<CommentQueryRepository>()
    val userRepository = mockk<UserRepository>()
    val postRepository = mockk<PostRepository>()
    val lectureRepository = mockk<LectureRepository>()
    val enrollmentRepository = mockk<EnrollmentRepository>()
    val commentService = spyk(
        CommentService(
            commentRepository,
            commentQueryRepository,
            userRepository,
            postRepository,
            lectureRepository,
            enrollmentRepository
        )
    )

    afterTest { clearMocks(commentService) }
    describe("create") {
        context("parentCommentId 가 없는 createCommentDto 가 주어졌을 때") {
            val createCommentDto = CreateCommentDto(
                userId = 1,
                postId = 1,
                content = "content",
            )
            it("userId 에 해당하는 user 가 없으면 NoSuchElementException 을 던진다") {
                // given
                every { userRepository.findById(createCommentDto.userId) } returns Optional.empty()

                // when
                val exception = shouldThrow<NoSuchElementException> {
                    commentService.create(createCommentDto)
                }

                // then
                exception.message shouldBe "user not found with id: ${createCommentDto.userId}"
            }
            it("postId 에 해당하는 post 가 없으면 NoSuchElementException 을 던진다") {
                // given
                every { userRepository.findById(createCommentDto.userId) } returns Optional.of(mockk())
                every { postRepository.findByIdWithBoard(createCommentDto.postId) } returns null

                // when
                val exception = shouldThrow<NoSuchElementException> {
                    commentService.create(createCommentDto)
                }

                // then
                exception.message shouldBe "post not found with id: ${createCommentDto.postId}"
            }

            it("모든 조건을 만족하면 saveRootComment 를 호출하고 그 결과를 반환한다") {
                // given
                val user = mockk<User>()
                val post = mockk<Post>()
                val comment = mockk<Comment>()
                every { userRepository.findById(createCommentDto.userId) } returns Optional.of(user)
                every { postRepository.findByIdWithBoard(createCommentDto.postId) } returns post
                every { post.board.lecture } returns mockk<Lecture>()

                every { commentService.checkUserHasRightToComment(user, post.board.lecture) } returns Unit
                every { commentService.saveRootComment(createCommentDto, post, user) } returns comment

                // when
                commentService.create(createCommentDto)

                // then
                verify(exactly = 1) { commentService.saveRootComment(createCommentDto, post, user) }
                verify(exactly = 0) { commentService.saveBranchComment(any(), any(), any(), any()) }
            }
        }
        context("parentCommentId 가 있는 createCommentDto 가 주어졌을 때") {
            val createCommentDto = CreateCommentDto(
                userId = 1,
                postId = 1,
                content = "content",
                parentCommentId = 1,
            )
            it("모든 조건이 만족하면 saveBranchComment 를 호출하고 그 결과를 반환한다") {
                // given
                val user = mockk<User>()
                val post = mockk<Post>()
                val comment = mockk<Comment>()
                every { userRepository.findById(createCommentDto.userId) } returns Optional.of(user)
                every { postRepository.findByIdWithBoard(createCommentDto.postId) } returns post
                every { post.board.lecture } returns mockk<Lecture>()

                every { commentService.checkUserHasRightToComment(user, post.board.lecture) } returns Unit
                every {
                    commentService.saveBranchComment(
                        createCommentDto.parentCommentId!!,
                        createCommentDto.content,
                        post,
                        user
                    )
                } returns comment

                // when
                commentService.create(createCommentDto)

                // then
                verify(exactly = 0) { commentService.saveRootComment(any(), any(), any()) }
                verify(exactly = 1) {
                    commentService.saveBranchComment(
                        createCommentDto.parentCommentId!!,
                        createCommentDto.content,
                        post,
                        user
                    )
                }
            }
        }
    }
    describe("getCommentOfPost") {
        context("getCommentOfPostDto, pageable 이 주어졌을 때") {
            val getCommentOfPostDto = GetCommentOfPostDto(
                postId = 1,
            )
            val pageable = mockk<Pageable>()
            it("postId 에 해당하는 post 가 없으면 NoSuchElementException 을 던진다") {
                // given
                every { postRepository.existsById(getCommentOfPostDto.postId) } returns false

                // when
                val exception = shouldThrow<NoSuchElementException> {
                    commentService.getCommentOfPost(getCommentOfPostDto, pageable)
                }

                // then
                exception.message shouldBe "post not found with id: ${getCommentOfPostDto.postId}"
            }
            it("모든 조건을 만족하면 commentQueryRepository 의 findCommentOfPost 를 호출하고 그 결과를 반환한다") {
                // given
                val comments = mockk<Page<Comment>>()
                every { postRepository.existsById(getCommentOfPostDto.postId) } returns true
                every {
                    commentQueryRepository.findAllCommentsOfPost(
                        getCommentOfPostDto.postId,
                        pageable
                    )
                } returns comments

                // when
                val result = commentService.getCommentOfPost(getCommentOfPostDto, pageable)

                // then
                result shouldBe comments
            }
        }
    }
})