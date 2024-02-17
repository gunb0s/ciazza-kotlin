package gunb0s.toy.ciazzakotlin.enrollement.repository

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.user.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface EnrollmentRepository : JpaRepository<Enrollment, Long> {
    fun findByStudentAndLecture(student: Student, lecture: Lecture): Enrollment?
}