package gunb0s.toy.ciazzakotlin.user.repository

import gunb0s.toy.ciazzakotlin.user.entity.Educator
import org.springframework.data.jpa.repository.JpaRepository

interface EducatorRepository : JpaRepository<Educator, Long>