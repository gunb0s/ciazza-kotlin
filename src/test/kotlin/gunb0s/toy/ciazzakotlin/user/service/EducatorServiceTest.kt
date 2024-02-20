package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorDto
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.util.ReflectionTestUtils

class EducatorServiceTest(
) : FunSpec() {
    private val educatorRepository = mockk<EducatorRepository>()
    private val educatorService = EducatorService(educatorRepository)

    init {
        test("create") {
            // Given
            val createEducatorDto = CreateEducatorDto("test")
            val educator = Educator("test")
            ReflectionTestUtils.setField(educator, "id", 1L)
            every { educatorRepository.save(any()) } returns educator

            // When
            val result = educatorService.create(createEducatorDto)

            // Then
            result shouldBe educator.id
        }
    }
}