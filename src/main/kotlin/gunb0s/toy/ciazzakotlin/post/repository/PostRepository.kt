package gunb0s.toy.ciazzakotlin.post.repository

import gunb0s.toy.ciazzakotlin.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.user where p.id = :postId")
    fun findByIdWithUser(postId: Long): Post?
}