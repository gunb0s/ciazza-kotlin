package gunb0s.toy.ciazzakotlin.lecture.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.LectureSearchCondition
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.entity.QLecture.lecture
import gunb0s.toy.ciazzakotlin.lecture.entity.Semester
import gunb0s.toy.ciazzakotlin.user.entity.QEducator.educator
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class LectureQueryRepositoryImpl(
    em: EntityManager,
) : LectureQueryRepository {
    private val queryFactory: JPAQuery<Lecture> = JPAQuery(em)

    override fun findAllBySearchCondition(
        lectureSearchCondition: LectureSearchCondition,
        pageable: Pageable,
    ): Page<Lecture> {
        val results: List<Lecture> = queryFactory
            .select(lecture)
            .from(lecture)
            .join(lecture.educator, educator)
            .fetchJoin()
            .where(
                semesterEq(lectureSearchCondition.semester),
                educatorIdEq(lectureSearchCondition.educatorId),
                lectureCodeContains(lectureSearchCondition.lectureCode)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery: JPAQuery<Long> = queryFactory
            .select(lecture.count())
            .from(lecture)
            .where(
                semesterEq(lectureSearchCondition.semester),
                educatorIdEq(lectureSearchCondition.educatorId),
                lectureCodeContains(lectureSearchCondition.lectureCode)
            )

        return PageableExecutionUtils.getPage(results, pageable) { countQuery.fetchOne()!! }
    }

    private fun semesterEq(semester: Semester?): BooleanExpression? {
        return if (semester != null) lecture.semester.eq(semester) else null
    }

    private fun educatorIdEq(educatorId: Long?): BooleanExpression? {
        return if (educatorId != null) educator.id.eq(educatorId) else null
    }

    private fun lectureCodeContains(lectureCode: String?): BooleanExpression? {
        return if (lectureCode != null) lecture.lectureCode.contains(lectureCode) else null
    }
}