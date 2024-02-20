package gunb0s.toy.ciazzakotlin.comment.entity

import gunb0s.toy.ciazzakotlin.common.entity.BaseEntity
import gunb0s.toy.ciazzakotlin.post.entity.Post
import gunb0s.toy.ciazzakotlin.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Comment(
    content: String,
    post: Post,
    user: User,
    commentOrder: Int,
    depth: Int,
    commentGroupId: Long?,
    parentComment: Comment?,
) : BaseEntity() {
    companion object {
        fun createRootComment(
            content: String,
            commentOrder: Int,
            post: Post,
            user: User,
            commentGroupId: Long? = null,
        ): Comment {
            return Comment(
                content = content,
                post = post,
                user = user,
                commentOrder = commentOrder,
                depth = 0,
                commentGroupId = commentGroupId,
                parentComment = null
            )
        }

        fun createBranchComment(
            content: String,
            commentOrder: Int,
            post: Post,
            user: User,
            parentComment: Comment,
        ): Comment {
            return Comment(
                content = content,
                post = post,
                user = user,
                commentOrder = commentOrder,
                depth = parentComment.depth + 1,
                commentGroupId = parentComment.commentGroupId,
                parentComment = parentComment
            )
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Long? = null

    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post = post
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User = user
        protected set

    @Column(name = "comment_order")
    var commentOrder: Int = commentOrder
        protected set

    @Column()
    var depth: Int = depth
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    var parentComment: Comment? = parentComment
        protected set

    @Column(name = "comment_group_id")
    var commentGroupId: Long? = commentGroupId
        protected set

    fun setCommentGroupId(commentGroupId: Long) {
        if (this.commentGroupId != null) {
            throw IllegalStateException("CommentGroupId is already set")
        }
        this.commentGroupId = commentGroupId
    }
}