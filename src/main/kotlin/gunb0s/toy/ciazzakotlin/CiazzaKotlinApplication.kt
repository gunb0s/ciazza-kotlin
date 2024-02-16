package gunb0s.toy.ciazzakotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CiazzaKotlinApplication

fun main(args: Array<String>) {
    runApplication<CiazzaKotlinApplication>(*args)
}
