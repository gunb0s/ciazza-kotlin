package gunb0s.toy.ciazzakotlin.enrollement.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.enrollement.entity.QEnrollment.enrollment
import gunb0s.toy.ciazzakotlin.lecture.entity.QLecture.lecture
import gunb0s.toy.ciazzakotlin.lecture.entity.Semester
import gunb0s.toy.ciazzakotlin.user.controller.dto.StudentLectureSearchCondition
import gunb0s.toy.ciazzakotlin.user.entity.QEducator.educator
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class EnrollmentQueryRepositoryImpl(
    em: EntityManager,
) : EnrollmentQueryRepository {
    private val queryFactory: JPAQueryFactory = JPAQueryFactory(em)

    override fun searchEnrollment(
        studentId: Long,
        studentLectureSearchCondition: StudentLectureSearchCondition,
        pageable: Pageable,
    ): Page<Enrollment> {

        val results: List<Enrollment> = queryFactory
            .selectFrom(enrollment)
            .join(enrollment.lecture, lecture).fetchJoin()
            .join(lecture.educator, educator).fetchJoin()
            .where(
                enrollment.student.id.eq(studentId),
                semesterEq(studentLectureSearchCondition.semester),
                lectureCodeContains(studentLectureSearchCondition.lectureCode)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery: JPAQuery<Long> = queryFactory
            .select(enrollment.count())
            .from(enrollment)
            .where(
                enrollment.student.id.eq(studentId),
                semesterEq(studentLectureSearchCondition.semester),
                lectureCodeContains(studentLectureSearchCondition.lectureCode)
            )
        return PageableExecutionUtils.getPage(results, pageable) {
            countQuery.fetchOne() ?: 0L
        }
    }

    private fun semesterEq(semester: Semester?): BooleanExpression? {
        return if (semester != null) lecture.semester.eq(semester) else null
    }

    private fun lectureCodeContains(lectureCode: String?): BooleanExpression? {
        return if (lectureCode != null) lecture.lectureCode.contains(lectureCode) else null
    }
}