package gunb0s.toy.ciazzakotlin.user.repository

import gunb0s.toy.ciazzakotlin.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

}
