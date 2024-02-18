package gunb0s.toy.ciazzakotlin.comment.service

import gunb0s.toy.ciazzakotlin.comment.controller.dto.CreateCommentDto
import gunb0s.toy.ciazzakotlin.comment.controller.dto.GetCommentOfPostDto
import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.comment.repository.CommentQueryRepository
import gunb0s.toy.ciazzakotlin.comment.repository.CommentRepository
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentRepository
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

        if (user.dtype.equals("E")) {
            lectureRepository.findByIdAndEducator(lecture.id!!, user as Educator)
                ?: throw NoSuchElementException("lecture not found with id : $lecture.id and educator id :$user.id")
        } else {
            enrollmentRepository.findByStudentAndLecture(user as Student, lecture)
                ?: throw NoSuchElementException(("enrollment not found with student id: $user.id and lecture id : $lecture.id"))
        }

        createCommentDto.parentCommentId?.let {
            val parentComment: Comment = commentRepository.findById(createCommentDto.parentCommentId).orElseThrow {
                NoSuchElementException("parent comment not found with id: ${createCommentDto.parentCommentId}")
            }
            if (parentComment.isRootComment()) {
                return saveFirstCommentOfRoot(createCommentDto, parentComment, post, user, parentComment.depth)
            } else {
                val maxCommentOrderInParent: Int? = commentRepository
                    .maxCommentOrderByCommentGroupIdAndParentComment(parentComment.commentGroupId!!, parentComment)

                maxCommentOrderInParent?.let {
                    commentRepository.updateCommentOrderGoeThanOrder(
                        parentComment.commentGroupId!!,
                        maxCommentOrderInParent + 1
                    )
                    saveCommentOfParent(
                        createCommentDto,
                        post,
                        user,
                        parentComment,
                        maxCommentOrderInParent,
                        parentComment.depth
                    )
                }
                commentRepository.updateCommentOrderGoeThanOrder(
                    parentComment.commentGroupId!!,
                    parentComment.commentOrder + 1
                )
                return saveFirstCommentOfParent(
                    createCommentDto,
                    post,
                    user,
                    parentComment,
                    parentComment.depth
                )
            }
        }
        val saveComment: Comment = saveRootComment(createCommentDto, post, user)
        saveComment.setCommentGroupId(saveComment.id!!)
        return saveComment.id!!
    }

    fun getCommentOfPost(getCommentOfPostDto: GetCommentOfPostDto, pageable: Pageable): Page<Comment> {
        val postId: Long = getCommentOfPostDto.postId
        val exists: Boolean = postRepository.existsById(postId)
        if (!exists) {
            throw java.util.NoSuchElementException("post not found with id $postId")
        }

        return commentQueryRepository.findAllCommentsOfPost(postId, pageable)
    }

    private fun isFirstCommentOfParent(maxCommentOrderInParent: Int?): Boolean {
        return maxCommentOrderInParent == null
    }

    private fun saveCommentOfParent(
        createCommentDto: CreateCommentDto,
        post: Post,
        user: User,
        parentComment: Comment,
        maxCommentOrderInParent: Int,
        parentCommentDepth: Int,
    ): Long {
        val comment = Comment(
            content = createCommentDto.content,
            post = post,
            user = user,
            parentComment = parentComment,
            commentOrder = maxCommentOrderInParent + 1,
            depth = parentCommentDepth + 1,
            commentGroupId = parentComment.commentGroupId
        )

        return commentRepository.save(comment).id!!
    }

    private fun saveFirstCommentOfParent(
        createCommentDto: CreateCommentDto,
        post: Post,
        user: User,
        parentComment: Comment,
        parentCommentDepth: Int,
    ): Long {
        val comment = Comment(
            content = createCommentDto.content,
            post = post,
            user = user,
            parentComment = parentComment,
            commentOrder = parentComment.commentOrder + 1,
            depth = parentCommentDepth + 1,
            commentGroupId = parentComment.commentGroupId
        )

        return commentRepository.save(comment).id!!
    }

    private fun saveFirstCommentOfRoot(
        createCommentDto: CreateCommentDto,
        parentComment: Comment,
        post: Post,
        user: User,
        parentCommentDepth: Int,
    ): Long {
        val maxCommentOrder: Int = commentRepository
            .maxCommentOrderByCommentGroupId(parentComment.commentGroupId!!)
        val comment = Comment(
            content = createCommentDto.content,
            post = post,
            user = user,
            parentComment = parentComment,
            commentOrder = maxCommentOrder + 1,
            depth = parentCommentDepth + 1,
            commentGroupId = parentComment.commentGroupId
        )
        return commentRepository.save(comment).id!!
    }

    private fun saveRootComment(createCommentDto: CreateCommentDto, post: Post, user: User): Comment {
        val comment = Comment(
            content = createCommentDto.content,
            post = post,
            user = user,
            commentOrder = 0,
            depth = 0,
            commentGroupId = null,
            parentComment = null
        )
        val save: Comment = commentRepository.save(comment)
        return save
    }
}
