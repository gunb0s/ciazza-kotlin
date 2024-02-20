package gunb0s.toy.ciazzakotlin.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
class BaseEntity {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    var createdDate: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    var updatedDate: LocalDateTime = LocalDateTime.now()
        protected set
}
