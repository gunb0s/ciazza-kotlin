package gunb0s.toy.ciazzakotlin.enrollement.repository

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.user.controller.dto.StudentLectureSearchCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface EnrollmentQueryRepository {
    fun searchEnrollment(
        studentId: Long,
        studentLectureSearchCondition: StudentLectureSearchCondition,
        pageable: Pageable,
    ): Page<Enrollment>
}
