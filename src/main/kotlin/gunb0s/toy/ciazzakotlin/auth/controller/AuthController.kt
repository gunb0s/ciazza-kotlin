package gunb0s.toy.ciazzakotlin.auth.controller

import gunb0s.toy.ciazzakotlin.auth.controller.dto.LoginRequestDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

data class GithubAccessTokenRequest(
    val client_id: String,
    val client_secret: String,
    val code: String,
)

data class GithubAccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val scope: String,
)

@RestController
class AuthController(
    @Value("\${GITHUB_CLIENT_ID}")
    val clientId: String,
    @Value("\${GITHUB_CLIENT_SECRET}")
    val clientSecret: String,
) {
    @CrossOrigin
    @PostMapping("/auth/github")
    fun login(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<GithubAccessTokenResponse> {
        val defaultClient = RestClient.create()

        val requestUrl = "https://github.com/login/oauth/access_token"

        val githubAccessTokenRequest = GithubAccessTokenRequest(
            clientId,
            clientSecret,
            loginRequest.code
        )

        return defaultClient.post()
            .uri(requestUrl)
            .accept(APPLICATION_JSON)
            .body(githubAccessTokenRequest)
            .retrieve()
            .toEntity<GithubAccessTokenResponse>()
    }
}