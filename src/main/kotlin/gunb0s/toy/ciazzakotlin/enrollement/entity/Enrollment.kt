package gunb0s.toy.ciazzakotlin.enrollement.entity

import gunb0s.toy.ciazzakotlin.common.entity.BaseEntity
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.user.entity.Student
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_enrollment_user_lecture",
        columnNames = ["user_id", "lecture_id"]
    )]
)
class Enrollment(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    var student: Student? = null,

    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    val lecture: Lecture? = null,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    var id: Long? = null
}