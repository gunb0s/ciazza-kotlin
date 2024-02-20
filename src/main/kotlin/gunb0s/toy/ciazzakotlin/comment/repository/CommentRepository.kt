package gunb0s.toy.ciazzakotlin.comment.repository

import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.post = :post order by c.createdDate desc limit 1")
    fun findLatestCommentByPostId(post: Post): Comment?

    @Query("select max(c.commentOrder) from Comment c where c.commentGroupId = :commentGroupId and c.parentComment = :parentComment")
    fun maxCommentOrderByCommentGroupIdAndParentComment(commentGroupId: Long, parentComment: Comment): Int?

    @Modifying
    @Query("update Comment c set c.commentOrder = c.commentOrder + 1 where c.post = :post and c.commentOrder >= :order")
    fun updateCommentOrderGoeThanOrder(post: Post, order: Int): Int
}
