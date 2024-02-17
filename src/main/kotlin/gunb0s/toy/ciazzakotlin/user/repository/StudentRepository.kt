package gunb0s.toy.ciazzakotlin.user.repository

import gunb0s.toy.ciazzakotlin.user.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long>
