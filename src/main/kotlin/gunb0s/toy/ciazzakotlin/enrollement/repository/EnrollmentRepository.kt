package gunb0s.toy.ciazzakotlin.enrollement.repository

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import org.springframework.data.jpa.repository.JpaRepository

interface EnrollmentRepository : JpaRepository<Enrollment, Long>