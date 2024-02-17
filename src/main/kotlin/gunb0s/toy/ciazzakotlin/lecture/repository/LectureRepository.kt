package gunb0s.toy.ciazzakotlin.lecture.repository

import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LectureRepository : JpaRepository<Lecture, Long> {
    @Query("select l from Lecture l join fetch l.educator where l.id = :lectureId")
    fun findByIdWitEducator(lectureId: Long): Lecture?
}
