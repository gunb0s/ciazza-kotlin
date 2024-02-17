package gunb0s.toy.ciazzakotlin.comment.repository

import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("select max(c.commentOrder) from Comment c where c.commentGroupId = :commentGroupId")
    fun maxCommentOrderByCommentGroupId(commentGroupId: Long): Int

    @Query("select max(c.commentOrder) from Comment c where c.commentGroupId = :commentGroupId and c.parentComment = :parentComment")
    fun maxCommentOrderByCommentGroupIdAndParentComment(commentGroupId: Long, parentComment: Comment): Int?

    @Modifying
    @Query("update Comment c set c.commentOrder = c.commentOrder + 1 where c.commentGroupId = :commentGroupId and c.commentOrder >= :order")
    fun updateCommentOrderGoeThanOrder(commentGroupId: Long, order: Int): Int
}
