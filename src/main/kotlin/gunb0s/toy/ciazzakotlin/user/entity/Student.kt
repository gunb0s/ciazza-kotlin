package gunb0s.toy.ciazzakotlin.user.entity;

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("S")
class Student(
    name: String,
) : User(name)
