package gunb0s.toy.ciazzakotlin.comment.entity

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
) {
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
    var parentComment: Comment? = null
        protected set

    @Column(name = "comment_group_id")
    var commentGroupId: Long? = null
        protected set

    fun isRootComment(): Boolean {
        return depth == 0
    }

    fun setCommentGroupId(commentGroupId: Long) {
        if (this.commentGroupId != null) {
            throw IllegalStateException("CommentGroupId is already set")
        }
        this.commentGroupId = commentGroupId
    }
}