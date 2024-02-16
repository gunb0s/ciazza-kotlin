package gunb0s.toy.ciazzakotlin.user.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("E")
class Educator(
    name: String,
) : User(name)