package gunb0s.toy.ciazzakotlin.board.entity

import gunb0s.toy.ciazzakotlin.common.entity.BaseEntity
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Board(
    title: String,
    lecture: Lecture,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    val id: Long? = null

    var title: String = title
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    var lecture: Lecture = lecture
        protected set
}