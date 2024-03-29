package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateEducatorDto
import gunb0s.toy.ciazzakotlin.user.entity.Educator
import gunb0s.toy.ciazzakotlin.user.repository.EducatorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EducatorService(
    private val educatorRepository: EducatorRepository,
) {
    @Transactional
    fun create(createEducatorDto: CreateEducatorDto): Long {
        val educator = Educator(createEducatorDto.name)
        val save = educatorRepository.save(educator)
        return save.id!!
    }

    fun getList(pageable: Pageable): Page<Educator> {
        return educatorRepository.findAll(pageable)
    }

    fun get(id: Long): Educator {
        return educatorRepository.findById(id).orElseThrow { NoSuchElementException("educator not found with id $id") }
    }
}
