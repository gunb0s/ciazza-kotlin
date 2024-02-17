package gunb0s.toy.ciazzakotlin.comment.repository

import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentQueryRepository {
    fun findAllCommentsOfPost(postId: Long, pageable: Pageable): Page<Comment>

    fun findUserComments(userId: Long, pageable: Pageable): Page<Comment>
}
