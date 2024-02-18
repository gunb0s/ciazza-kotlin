package gunb0s.toy.ciazzakotlin.user.entity;

import gunb0s.toy.ciazzakotlin.enrollement.entity.Enrollment
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
@DiscriminatorValue("S")
class Student(
    name: String,
) : User(name) {
    @OneToMany(mappedBy = "student")
    val enrollments: MutableList<Enrollment> = mutableListOf()
}
