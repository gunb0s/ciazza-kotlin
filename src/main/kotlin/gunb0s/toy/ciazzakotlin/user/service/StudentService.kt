package gunb0s.toy.ciazzakotlin.user.service

import gunb0s.toy.ciazzakotlin.user.controller.dto.CreateStudentDto
import gunb0s.toy.ciazzakotlin.user.entity.Student
import gunb0s.toy.ciazzakotlin.user.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StudentService(
    private val studentRepository: StudentRepository,
) {
    @Transactional
    fun create(createEducatorDto: CreateStudentDto): Long {
        val student = Student(
            name = createEducatorDto.name
        )
        studentRepository.save(student)
        return student.id!!
    }
}