package gunb0s.toy.ciazzakotlin.lecture.entity

import gunb0s.toy.ciazzakotlin.board.entity.Board
import gunb0s.toy.ciazzakotlin.common.entity.BaseEntity
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.Random

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_lecture_registration_code",
        columnNames = ["registration_code"]
    ), UniqueConstraint(name = "uk_lecture_lecture_code_and_semester", columnNames = ["lecture_code", "semester"])]
)
class Lecture(
    name: String,
    lectureCode: String,
    educator: Educator,
    semester: Semester,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    val id: Long? = null

    @Column()
    var name: String = name
        protected set

    @Column()
    var lectureCode: String = lectureCode
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var educator: Educator = educator
        protected set

    @Enumerated(EnumType.STRING)
    var semester: Semester = semester
        protected set

    @Column(
        length = 8,
        nullable = false
    )
    var registrationCode: String? = null
        protected set

    @OneToMany(mappedBy = "lecture")
    protected val mutableBoards: MutableList<Board> = mutableListOf()

    val boards: List<Board>
        get() = mutableBoards.toList()

    @PrePersist
    fun generateRegistrationCode() {
        this.registrationCode =
            Random().nextInt(10000000, 100000000 - 1).toString()
    }
}
