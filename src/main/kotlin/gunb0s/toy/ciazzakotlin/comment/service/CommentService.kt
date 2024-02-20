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
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.entity.User
import gunb0s.toy.ciazzakotlin.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val commentQueryRepository: CommentQueryRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val lectureRepository: LectureRepository,
    private val enrollmentRepository: EnrollmentRepository,
) {
    @Transactional
    fun create(createCommentDto: CreateCommentDto): Long {
        val user = userRepository.findById(createCommentDto.userId)
            .orElseThrow { NoSuchElementException("user not found with id: ${createCommentDto.userId}") }
        val post = postRepository.findByIdWithBoard(createCommentDto.postId) ?: throw NoSuchElementException(
            "post not found with id: $createCommentDto.postId"
        )
        val lecture = post.board.lecture

        checkUserHasRightToComment(user, lecture)

        createCommentDto.parentCommentId?.let {
            return saveBranchComment(
                createCommentDto.parentCommentId,
                createCommentDto.content,
                post,
                user
            )
        }
        return saveRootComment(createCommentDto, post, user)
    }

    private fun checkUserHasRightToComment(
        user: User,
        lecture: Lecture,
    ) {
        if (user.dtype.equals("E")) {
            lectureRepository.findByIdAndEducator(lecture.id!!, user as Educator)
                ?: throw NoSuchElementException("lecture not found with id : $lecture.id and educator id :$user.id")
        } else {
            enrollmentRepository.findByStudentAndLecture(user as Student, lecture)
                ?: throw NoSuchElementException(("enrollment not found with student id: $user.id and lecture id : $lecture.id"))
        }
    }

    private fun saveRootComment(
        createCommentDto: CreateCommentDto,
        post: Post,
        user: User,
    ): Long {
        val recentComment = commentRepository.findLatestCommentByPostId(post)
        recentComment?.let {
            val comment = Comment.createRootComment(
                content = createCommentDto.content,
                commentOrder = recentComment.commentOrder + 1,
                post = post,
                user = user,
                commentGroupId = recentComment.commentGroupId
            )
            val saveComment = commentRepository.save(comment)
            return saveComment.id!!
        }

        val comment = Comment.createRootComment(
            content = createCommentDto.content,
            commentOrder = 0,
            post = post,
            user = user,
        )
        val saveComment = commentRepository.save(comment)
        saveComment.setCommentGroupId(saveComment.id!!)
        return saveComment.id!!
    }

    private fun saveBranchComment(
        parentCommentId: Long,
        content: String,
        post: Post,
        user: User,
    ): Long {
        val parentComment = commentRepository.findById(parentCommentId)
            .orElseThrow { NoSuchElementException("parent comment not found with id: $parentCommentId") }

        val maxCommentOrder = commentRepository.maxCommentOrderByCommentGroupIdAndParentComment(
            parentComment.commentGroupId!!,
            parentComment
        ) ?: parentComment.commentOrder

        val comment = Comment.createBranchComment(
            content = content,
            commentOrder = maxCommentOrder + 1,
            post = post,
            user = user,
            parentComment = parentComment
        )

        commentRepository.updateCommentOrderGoeThanOrder(post, maxCommentOrder + 1)
        commentRepository.save(comment)

        return comment.id!!
    }

    fun getCommentOfPost(getCommentOfPostDto: GetCommentOfPostDto, pageable: Pageable): Page<Comment> {
        val postId: Long = getCommentOfPostDto.postId
        val exists: Boolean = postRepository.existsById(postId)
        if (!exists) {
            throw java.util.NoSuchElementException("post not found with id $postId")
        }

        return commentQueryRepository.findAllCommentsOfPost(postId, pageable)
    }
}
