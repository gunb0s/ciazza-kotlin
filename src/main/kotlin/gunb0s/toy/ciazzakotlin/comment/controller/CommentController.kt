package gunb0s.toy.ciazzakotlin.comment.controller

import gunb0s.toy.ciazzakotlin.comment.controller.dto.CommentDto
import gunb0s.toy.ciazzakotlin.comment.controller.dto.CreateCommentDto
import gunb0s.toy.ciazzakotlin.comment.controller.dto.CreateCommentResponseDto
import gunb0s.toy.ciazzakotlin.comment.controller.dto.GetCommentOfPostDto
import gunb0s.toy.ciazzakotlin.comment.entity.Comment
import gunb0s.toy.ciazzakotlin.comment.service.CommentService
import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.common.exception.ErrorResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "comment", description = "the comment API")
class CommentController(
    private val commentService: CommentService,
) {
    @Operation(summary = "add comment", description = "add a new comment to post", tags = ["comment"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @PostMapping("/comment")
    fun createComment(@RequestBody createCommentDto: @Valid CreateCommentDto): ResponseEntity<ResponseDto<CreateCommentResponseDto>> {
        val id: Long = commentService.create(createCommentDto)
        val responseDto =
            ResponseDto.ok(CreateCommentResponseDto(id))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto)
    }

    @Operation(summary = "get comments", description = "get comment of a post with pagination", tags = ["comment"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Entity not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @GetMapping("/comment")
    fun getCommentOfPost(
        @ParameterObject getCommentOfPostDto: GetCommentOfPostDto,
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<ResponseDto<Page<CommentDto>>> {
        val comments: Page<Comment> = commentService.getCommentOfPost(
            getCommentOfPostDto,
            pageable!!
        )
        val responseDto: ResponseDto<Page<CommentDto>> =
            ResponseDto.ok(comments.map {
                CommentDto(it)
            })
        return ResponseEntity
            .ok<ResponseDto<Page<CommentDto>>>(responseDto)
    }
}