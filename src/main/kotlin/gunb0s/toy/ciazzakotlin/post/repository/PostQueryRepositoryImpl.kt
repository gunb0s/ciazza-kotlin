package gunb0s.toy.ciazzakotlin.post.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import gunb0s.toy.ciazzakotlin.board.entity.QBoard.board
import gunb0s.toy.ciazzakotlin.post.controller.dto.PostSearchCondition
import gunb0s.toy.ciazzakotlin.post.entity.Post
import gunb0s.toy.ciazzakotlin.post.entity.QPost.post
import gunb0s.toy.ciazzakotlin.user.entity.QUser.user
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class PostQueryRepositoryImpl(
    em: EntityManager,
) : PostQueryRepository {
    private val queryFactory: JPAQuery<Post> = JPAQuery(em)

    override fun search(
        lectureId: Long,
        boardId: Long,
        postSearchCondition: PostSearchCondition,
        pageable: Pageable,
    ): Page<Post> {
        val results: List<Post> = queryFactory
            .select(post)
            .from(post)
            .join(post.board, board)
            .join(post.user, user).fetchJoin()
            .where(
                lectureEq(lectureId),
                boardEq(boardId),
                titleContains(postSearchCondition.title),
                contentContains(postSearchCondition.content),
                userEq(postSearchCondition.userId)
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        val countQuery: JPAQuery<Long> = queryFactory.select(post.count())
            .join(post.board, board)
            .where(
                lectureEq(lectureId),
                boardEq(boardId),
                titleContains(postSearchCondition.title),
                contentContains(postSearchCondition.content),
                userEq(postSearchCondition.userId)
            )
        return PageableExecutionUtils.getPage(results, pageable) { countQuery.fetchOne() ?: 0L }
    }

    private fun contentContains(content: String?): BooleanExpression? {
        return if (content != null) post.content.contains(content) else null
    }

    private fun userEq(userId: Long?): BooleanExpression? {
        return if (userId != null) post.user.id.eq(userId) else null
    }

    private fun titleContains(title: String?): BooleanExpression? {
        return if (title != null) post.title.contains(title) else null
    }

    private fun boardEq(boardId: Long?): BooleanExpression? {
        return if (boardId != null) board.id.eq(boardId) else null
    }

    private fun lectureEq(lectureId: Long?): BooleanExpression? {
        return if (lectureId != null) post.board.lecture.id.eq(lectureId) else null
    }
}