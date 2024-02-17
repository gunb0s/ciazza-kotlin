package gunb0s.toy.ciazzakotlin.user.controller

import gunb0s.toy.ciazzakotlin.comment.controller.dto.CommentDto
import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "User", description = "User API")
class UserController(
    private val userService: UserService,
) {

    @Operation(summary = "get comments", description = "get comment of a user with pagination", tags = ["user"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @GetMapping("/user/{userId}/comment")
    fun getUserComments(
        @PathVariable userId: Long,
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<ResponseDto<Page<CommentDto>>> {
        val userComments: Page<Comment> = userService.getUserComments(userId, pageable)
        val responseDto: ResponseDto<Page<CommentDto>> = ResponseDto.ok(
            userComments.map {
                CommentDto(it)
            })
        return ResponseEntity
            .ok<ResponseDto<Page<CommentDto>>>(responseDto)
    }
}