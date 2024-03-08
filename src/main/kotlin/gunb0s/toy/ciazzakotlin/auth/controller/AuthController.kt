package gunb0s.toy.ciazzakotlin.auth.controller

import gunb0s.toy.ciazzakotlin.auth.controller.dto.LoginRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val authenticationManager: AuthenticationManager,
) {
    @PostMapping("/auth/login")
    fun login(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<Void> {
        val authenticateRequest = UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username,
            loginRequest.password
        )
        val authenticationResponse = authenticationManager.authenticate(authenticateRequest)

        return ResponseEntity.ok().build()
    }
}