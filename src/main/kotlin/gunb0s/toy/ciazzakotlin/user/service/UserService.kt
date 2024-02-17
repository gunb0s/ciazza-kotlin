package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.comment.repository.CommentQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val commentQueryRepository: CommentQueryRepository,
) {
    fun getUserComments(userId: Long, pageable: Pageable): Page<Comment> {
        return commentQueryRepository.findUserComments(userId, pageable)
    }

}
