package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentQueryRepository
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.StudentLectureSearchCondition
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.repository.StudentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StudentService(
    private val studentRepository: StudentRepository,
    private val enrollmentQueryRepository: EnrollmentQueryRepository,
) {
    @Transactional
    fun create(createEducatorDto: CreateStudentDto): Long {
        val student = Student(
            name = createEducatorDto.name
        )
        val save = studentRepository.save(student)
        return save.id!!
    }

    fun getList(pageable: Pageable): Page<Student> {
        return studentRepository.findAll(pageable)
    }

    fun get(id: Long): Student {
        return studentRepository.findById(id).orElseThrow {
            NoSuchElementException("Student not found with id $id")
        }
    }

    fun getLectures(
        id: Long,
        studentLectureSearchCondition: StudentLectureSearchCondition,
        pageable: Pageable,
    ): Page<Enrollment> {
        return enrollmentQueryRepository.searchEnrollment(id, studentLectureSearchCondition, pageable)
    }
}