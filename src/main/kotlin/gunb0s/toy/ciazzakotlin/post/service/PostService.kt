package gunb0s.toy.ciazzakotlin.post.service

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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val postQueryRepository: PostQueryRepository,
    private val userRepository: UserRepository,
    private val lectureRepository: LectureRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val boardRepository: BoardRepository,
) {
    @Transactional
    fun create(createPostDto: CreatePostDto): Long {
        val userId: Long = createPostDto.userId
        val lectureId: Long = createPostDto.lectureId

        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("user not found with userId: $userId") }

        if (user.dtype.equals("E")) {
            lectureRepository.findByIdAndEducator(lectureId, user as Educator)
                ?: throw NoSuchElementException("lecture not found with lectureId: $lectureId and educatorId: $userId")
        } else {
            val lecture = lectureRepository.findById(lectureId)
                .orElseThrow { NoSuchElementException("lecture not found with lectureId: $lectureId") }
            enrollmentRepository.findByStudentAndLecture(user as Student, lecture)
                ?: throw NoSuchElementException("enrollment not found with studentId: $userId and lectureId: $lectureId")
        }

        val board = boardRepository.findById(createPostDto.boardId)
            .orElseThrow { NoSuchElementException("board not found with boardId: $createPostDto.boardId") }

        require(board.lecture.id!! == lectureId) { "The board does not belong to the lecture." }

        val post = Post(
            title = createPostDto.title,
            content = createPostDto.content,
            board = board,
            user = user
        )

        postRepository.save(post)
        return post.id!!
    }

    fun get(postId: Long): Post {
        return postRepository.findByIdWithUser(postId)
            ?: throw NoSuchElementException("post not found with postId: $postId")
    }

    fun getList(
        postListDto: GetPostListDto,
        postSearchCondition: PostSearchCondition,
        pageable: Pageable,
    ): Page<Post> {
        return postQueryRepository.search(
            postListDto.lectureId,
            postListDto.boardId,
            postSearchCondition,
            pageable
        )
    }
}
