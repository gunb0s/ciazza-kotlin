package gunb0s.toy.ciazzakotlin.lecture.entity

import gunb0s.toy.ciazzakotlin.common.entity.BaseEntity
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_lecture_registration_code",
        columnNames = ["registration_code"]
    ), UniqueConstraint(name = "uk_lecture_lecture_code_and_semester", columnNames = ["lecture_code", "semester"])]
)
class Lecture(
    val name: String,

    val lectureCode: String,

    @Column(
        length = 8
    )
    var registrationCode: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val educator: Educator,

    @Enumerated(EnumType.STRING)
    val semester: Semester,
) : BaseEntity() {
    @Id
    @GeneratedValue
    @Column(name = "lecture_id")
    var id: Long? = null
}
