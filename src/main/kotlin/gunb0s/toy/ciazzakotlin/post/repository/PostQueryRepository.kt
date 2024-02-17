package gunb0s.toy.ciazzakotlin.post.repository

import gunb0s.toy.ciazzakotlin.post.controller.dto.PostSearchCondition
import gunb0s.toy.ciazzakotlin.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostQueryRepository {
    fun search(
        lectureId: Long,
        boardId: Long,
        postSearchCondition: PostSearchCondition,
        pageable: Pageable,
    ): Page<Post>
}
