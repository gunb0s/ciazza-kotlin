package gunb0s.toy.ciazzakotlin.lecture.service

import gunb0s.toy.ciazzakotlin.common.exception.InvalidRegistrationCodeException
import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentRepository
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.CreateLectureDto
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.EnrollLectureDto
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.LectureSearchCondition
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureQueryRepository
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureRepository
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import gunb0s.toy.ciazzakotlin.user.repository.StudentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LectureService(
    private val lectureRepository: LectureRepository,
    private val lectureQueryRepository: LectureQueryRepository,
    private val educatorRepository: EducatorRepository,
    private val studentRepository: StudentRepository,
    private val enrollmentRepository: EnrollmentRepository,
) {
    @Transactional
    fun createLecture(createLectureDto: CreateLectureDto): Long {
        val educator: Educator = educatorRepository.findById(createLectureDto.educatorId).orElseThrow {
            NoSuchElementException(
                "educator not found with id: $createLectureDto.educatorId"
            )
        }
        val lecture = Lecture(
            name = createLectureDto.name,
            lectureCode = createLectureDto.lectureCode,
            educator = educator,
            semester = createLectureDto.semester
        )

        return lectureRepository.save(lecture).id!!
    }

    @Transactional
    fun enroll(lectureId: Long, enrollLectureDto: EnrollLectureDto): Long {
        val lecture = lectureRepository.findById(lectureId)
            .orElseThrow { NoSuchElementException("lecture not found with id, $lectureId") }
        val student: Student = studentRepository.findById(enrollLectureDto.studentId).orElseThrow {
            NoSuchElementException(
                "student not found with id, " + enrollLectureDto.studentId
            )
        }

        if (!lecture.registrationCode.equals(enrollLectureDto.registrationCode)) {
            throw InvalidRegistrationCodeException("Invalid registration code")
        }

        val enrollment: Enrollment = Enrollment(
            lecture = lecture,
            student = student
        )

        enrollmentRepository.save(enrollment)
        return enrollment.id!!
    }

    fun getList(lectureSearchCondition: LectureSearchCondition, pageable: Pageable): Page<Lecture> {
        return lectureQueryRepository.findAllBySearchCondition(lectureSearchCondition, pageable)
    }

    fun get(lectureId: Long): Lecture {
        return lectureRepository.findByIdWitEducator(lectureId)
            ?: throw NoSuchElementException("lecture not found with id, $lectureId")
    }
}
