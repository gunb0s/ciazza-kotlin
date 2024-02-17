package gunb0s.toy.ciazzakotlin.lecture.repository

import gunb0s.toy.ciazzakotlin.lecture.controller.dto.LectureSearchCondition
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LectureQueryRepository {
    fun findAllBySearchCondition(lectureSearchCondition: LectureSearchCondition, pageable: Pageable): Page<Lecture>
}
