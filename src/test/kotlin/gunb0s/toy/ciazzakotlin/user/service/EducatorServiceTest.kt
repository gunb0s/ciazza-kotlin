package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorDto
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.test.util.ReflectionTestUtils
import java.util.Optional

class EducatorServiceTest(
) : DescribeSpec({
    val educatorRepository = mockk<EducatorRepository>()
    val educatorService = EducatorService(educatorRepository)

    describe("create") {
        context("name 이 주어지면") {
            it("educator 를 저장하고 id 를 반환한다") {
                // given
                val name = "name"
                val createEducatorDto = CreateEducatorDto(
                    name = name,
                )
                val educator = Educator(name)
                ReflectionTestUtils.setField(educator, "id", 1L)
                every { educatorRepository.save(any()) } returns educator

                // when
                val id = educatorService.create(createEducatorDto)

                // then
                id shouldBe 1L
            }
        }
    }

    describe("getList") {
        context("pageable 이 주어지면") {
            it("educator 를 조회하여 반환한다") {
                // given
                val pageable = mockk<Pageable>()
                val page = mockk<Page<Educator>>()
                every { educatorRepository.findAll(pageable) } returns page

                // when
                val result = educatorService.getList(pageable)

                // then
                result shouldBe page
            }
        }
    }

    describe("get") {
        context("id 가 주어지면") {
            it("id 에 해당하는 educator 를 조회하여 반환한다") {
                // given
                val id = 1L
                val educator = Educator("name")
                every { educatorRepository.findById(id) } returns Optional.of(educator)

                // when
                val result = educatorService.get(id)

                // then
                result shouldBe educator
            }

            it("id 에 해당하는 educator 가 없으면 NoSuchElementException 을 던진다") {
                // given
                val id = 1L
                every { educatorRepository.findById(id) } returns Optional.empty()

                // when

                // then
                shouldThrow<NoSuchElementException> {
                    educatorService.get(id)
                }
            }
        }
    }
})