package gunb0s.toy.ciazzakotlin.post.controller

import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.common.exception.ErrorResponseDto
import gunb0s.toy.ciazzakotlin.post.controller.dto.CreatePostResponseDto
import gunb0s.toy.ciazzakotlin.post.controller.dto.GetPostListDto
import gunb0s.toy.ciazzakotlin.post.controller.dto.PostDto
import gunb0s.toy.ciazzakotlin.post.controller.dto.PostSearchCondition
import gunb0s.toy.ciazzakotlin.post.dto.CreatePostDto
import gunb0s.toy.ciazzakotlin.post.entity.Post
import gunb0s.toy.ciazzakotlin.post.service.PostService
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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "post", description = "the post API")
class PostController(
    private val postService: PostService,
) {
    @Operation(summary = "Add post", description = "add a new post to lecture", tags = ["post"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "400",
            description = "post not belong to lecture",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @PostMapping("/post")
    fun create(@RequestBody createPostDto: @Valid CreatePostDto): ResponseEntity<ResponseDto<CreatePostResponseDto>> {
        val id: Long = postService.create(createPostDto)
        val responseDto: ResponseDto<CreatePostResponseDto> = ResponseDto.created(CreatePostResponseDto(id))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto)
    }

    @Operation(summary = "get post detail", description = "get post by id", tags = ["post"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Post not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @GetMapping("/post/{postId}")
    fun get(@PathVariable postId: Long): ResponseEntity<ResponseDto<PostDto>> {
        val post = postService.get(postId)
        val responseDto: ResponseDto<PostDto> = ResponseDto.ok(PostDto(post))
        return ResponseEntity
            .ok<ResponseDto<PostDto>>(responseDto)
    }

    @Operation(summary = "get post", description = "get post with pagination", tags = ["post"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @GetMapping("/post")
    fun getList(
        @ParameterObject getPostListDto: @Valid GetPostListDto,
        @ParameterObject postSearchCondition: PostSearchCondition,
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<ResponseDto<Page<PostDto>>> {
        val list: Page<Post> = postService.getList(getPostListDto, postSearchCondition, pageable)
        val responseDto: ResponseDto<Page<PostDto>> = ResponseDto.ok(list.map {
            PostDto(it)
        })
        return ResponseEntity
            .ok(responseDto)
    }
}