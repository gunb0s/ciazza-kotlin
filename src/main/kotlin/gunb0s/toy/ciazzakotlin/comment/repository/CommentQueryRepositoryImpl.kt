package gunb0s.toy.ciazzakotlin.comment.repository

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.comment.entity.QComment.comment
import gunb0s.toy.ciazzakotlin.user.entity.QUser.user
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class CommentQueryRepositoryImpl(
    em: EntityManager,
) : CommentQueryRepository {
    private val queryFactory: JPAQueryFactory = JPAQueryFactory(em)

    override fun findAllCommentsOfPost(postId: Long, pageable: Pageable): Page<Comment> {
        val result: List<Comment> = queryFactory
            .selectFrom(comment)
            .join(comment.user, user).fetchJoin()
            .where(comment.post.id.eq(postId))
            .orderBy(comment.commentGroupId.asc(), comment.commentOrder.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery: JPAQuery<Long> = queryFactory
            .select(comment.count())
            .from(comment)
            .where(comment.post.id.eq(postId))

        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetchOne() ?: 0L }
    }

    override fun findUserComments(userId: Long, pageable: Pageable): Page<Comment> {
        val results: List<Comment> = queryFactory
            .select(comment)
            .from(comment)
            .join(comment.user, user).fetchJoin()
            .where(comment.user.id.eq(userId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = queryFactory
            .select(comment.count())
            .where(comment.user.id.eq(userId))
        return PageableExecutionUtils.getPage(results, pageable) { countQuery.fetchOne() ?: 0L }
    }
}