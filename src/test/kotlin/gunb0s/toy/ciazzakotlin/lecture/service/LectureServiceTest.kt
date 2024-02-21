package gunb0s.toy.ciazzakotlin.lecture.service

import gunb0s.toy.ciazzakotlin.common.exception.InvalidRegistrationCodeException
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentRepository
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.CreateLectureDto
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.EnrollLectureDto
import gunb0s.toy.ciazzakotlin.lecture.controller.dto.LectureSearchCondition
import gunb0s.toy.ciazzakotlin.lecture.entity.Lecture
import gunb0s.toy.ciazzakotlin.lecture.entity.Semester
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureQueryRepository
import gunb0s.toy.ciazzakotlin.lecture.repository.LectureRepository
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import gunb0s.toy.ciazzakotlin.user.repository.StudentRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

class LectureServiceTest : DescribeSpec({
    val lectureRepository = mockk<LectureRepository>()
    val lectureQueryRepository = mockk<LectureQueryRepository>()
    val educatorRepository = mockk<EducatorRepository>()
    val studentRepository = mockk<StudentRepository>()
    val enrollmentRepository = mockk<EnrollmentRepository>()

    val lectureService = LectureService(
        lectureRepository,
        lectureQueryRepository,
        educatorRepository,
        studentRepository,
        enrollmentRepository
    )

    describe("create") {
        context("createLectureDto 가 주어졌을 때") {
            it("educatorId 로 educator 를 찾을 수 없다면 NoSuchElementException 을 던진다") {
                // given
                val createLectureDto = CreateLectureDto(
                    name = "name",
                    lectureCode = "lectureCode",
                    educatorId = 1L,
                    semester = Semester.SPRING_2024
                )
                every { educatorRepository.findById(createLectureDto.educatorId) } returns Optional.empty()

                // when
                // then
                shouldThrow<NoSuchElementException> {
                    lectureService.create(createLectureDto)
                }
            }

            it("educatorId 로 educator 를 찾을 수 있다면 lecture 를 생성하고 id 를 반환한다") {
                // given
                val createLectureDto = CreateLectureDto(
                    name = "name",
                    lectureCode = "lectureCode",
                    educatorId = 1L,
                    semester = Semester.SPRING_2024
                )
                val educator = mockk<Educator>()
                every { educatorRepository.findById(createLectureDto.educatorId) } returns Optional.of(educator)
                every { lectureRepository.save(any()) } returns mockk()

                // when
                val result = lectureService.create(createLectureDto)

                // then
                result.name shouldBe createLectureDto.name
                result.lectureCode shouldBe createLectureDto.lectureCode
                result.educator shouldBe educator
            }
        }
    }
    describe("enroll") {
        context("lectureId 와 enrollLectureDto 가 주어졌을 때") {
            val lectureId = 1L
            val enrollLectureDto = EnrollLectureDto(
                studentId = 1L,
                registrationCode = "registrationCode"
            )
            it("lectureId 로 lecture 를 찾을 수 없다면 NoSuchElementException 을 던진다") {
                // given
                every { lectureRepository.findById(lectureId) } returns Optional.empty()

                // when
                // then
                shouldThrow<NoSuchElementException> {
                    lectureService.enroll(lectureId, enrollLectureDto)
                }
            }

            it("studentId 로 student 를 찾을 수 없다면 NoSuchElementException 을 던진다") {
                // given
                every { lectureRepository.findById(lectureId) } returns Optional.of(mockk())
                every { studentRepository.findById(enrollLectureDto.studentId) } returns Optional.empty()

                // when
                // then
                shouldThrow<NoSuchElementException> {
                    lectureService.enroll(lectureId, enrollLectureDto)
                }
            }

            it(
                """
                lecture 의 registration code 가 dto 로 받은 registration code 와 다르다면
                 InvalidRegistrationCodeException 을 던진다
            """.trimIndent()
            ) {
                // given
                val lecture = mockk<Lecture>()
                every { lecture.registrationCode } returns "differentRegistrationCode"
                every { lectureRepository.findById(lectureId) } returns Optional.of(lecture)
                every { studentRepository.findById(enrollLectureDto.studentId) } returns Optional.of(mockk<Student>())

                // when
                // then
                shouldThrow<InvalidRegistrationCodeException> {
                    lectureService.enroll(lectureId, enrollLectureDto)
                }
            }

            it("모든 조건을 만족한다면 enrollment 을 생성하고 id 를 반환한다") {
                // given
                val lecture = mockk<Lecture>()
                val student = mockk<Student>()
                every { lecture.registrationCode } returns enrollLectureDto.registrationCode
                every { lectureRepository.findById(lectureId) } returns Optional.of(lecture)
                every { studentRepository.findById(enrollLectureDto.studentId) } returns Optional.of(student)
                every { enrollmentRepository.save(any()) } returns mockk()

                // when
                val result = lectureService.enroll(lectureId, enrollLectureDto)

                // then
                result.lecture shouldBe lecture
                result.student shouldBe student
            }
        }
    }
    describe("getList") {
        context("lectureSearchCondition 과 pageable 이 주어졌을 때") {
            it("lectureQueryRepository 를 통해 조회한 결과를 반환한다") {
                // given
                val lectureSearchCondition = mockk<LectureSearchCondition>()
                val pageable = mockk<Pageable>()
                val page = mockk<Page<Lecture>>()
                every { lectureQueryRepository.findAllBySearchCondition(lectureSearchCondition, pageable) } returns page

                // when
                val result = lectureService.getList(lectureSearchCondition, pageable)

                // then
                result shouldBe page
            }
        }
    }
    describe("get") {
        context("lectureId 가 주어졌을 때") {
            it("lectureRepository 를 통해 조회한 결과가 없다면 NoSuchElementException 을 던진다") {
                // given
                val lectureId = 1L
                every { lectureRepository.findByIdWitEducator(lectureId) } returns null

                // when
                // then
                shouldThrow<NoSuchElementException> {
                    lectureService.get(lectureId)
                }
            }
            it("lectureRepository 를 통해 조회한 결과를 반환한다") {
                // given
                val lectureId = 1L
                val lecture = mockk<Lecture>()
                every { lectureRepository.findByIdWitEducator(lectureId) } returns lecture

                // when
                val result = lectureService.get(lectureId)

                // then
                result shouldBe lecture
            }
        }
    }
})