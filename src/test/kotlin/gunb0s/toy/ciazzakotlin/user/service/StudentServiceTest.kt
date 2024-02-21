package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import gunb0s.toy.ciazzakotlin.enrollement.repository.EnrollmentQueryRepository
import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentDto
import gunb0s.toy.ciazzakotlin.user.controller.dto.StudentLectureSearchCondition
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.repository.StudentRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

class StudentServiceTest : DescribeSpec({
    val studentRepository = mockk<StudentRepository>()
    val enrollmentQueryRepository = mockk<EnrollmentQueryRepository>()
    val studentService = StudentService(studentRepository, enrollmentQueryRepository)

    describe("create") {
        context("name 이 주어지면") {
            it("student 를 저장하고 id 를 반환한다") {
                // given
                val name = "name"
                val createStudentDto = CreateStudentDto(
                    name = name,
                )
                val student = Student(name)
                ReflectionTestUtils.setField(student, "id", 1L)
                every { studentRepository.save(any()) } returns student

                // when
                val id = studentService.create(createStudentDto)

                // then
                id shouldBeEqual 1L
            }
        }
    }

    describe("getList") {
        context("pageable 이 주어지면") {
            it("student 들을 조회하여 반환한다") {
                // given
                val pageable = mockk<Pageable>()
                val page = mockk<Page<Student>>()
                every { studentRepository.findAll(pageable) } returns page

                // when
                val result = studentService.getList(pageable)

                // then
                result shouldBe page
            }
        }
    }

    describe("get") {
        context("id 가 주어지면") {
            it("id 에 해당하는 student 를 조회하여 반환한다") {
                // given
                val id = 1L
                val educator = Student("name")
                every { studentRepository.findById(id) } returns Optional.of(educator)

                // when
                val result = studentService.get(id)

                // then
                result shouldBe educator
            }

            it("id 에 해당하는 educator 가 없으면 NoSuchElementException 을 던진다") {
                // given
                val id = 1L
                every { studentRepository.findById(id) } returns Optional.empty()

                // when

                // then
                shouldThrow<NoSuchElementException> {
                    studentService.get(id)
                }
            }
        }
    }

    describe("getLectures") {
        context("id, studentLectureSearchCondition, pageable 이 주어지면") {
            it("id 에 해당하는 student 의 lecture 들을 조회하여 반환한다") {
                // given
                val id = 1L
                val studentLectureSearchCondition = mockk<StudentLectureSearchCondition>()
                val pageable = mockk<Pageable>()
                val page = mockk<Page<Enrollment>>()
                every {
                    enrollmentQueryRepository.searchEnrollment(
                        id,
                        studentLectureSearchCondition,
                        pageable
                    )
                } returns page

                // when
                val result = studentService.getLectures(id, studentLectureSearchCondition, pageable)

                // then
                result shouldBe page
            }
        }
    }
})