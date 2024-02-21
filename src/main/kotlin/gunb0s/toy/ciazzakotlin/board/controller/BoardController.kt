package gunb0s.toy.ciazzakotlin.board.controller

import gunb0s.toy.ciazzakotlin.board.controller.dto.BoardDto
import gunb0s.toy.ciazzakotlin.board.controller.dto.CreateBoardDto
import gunb0s.toy.ciazzakotlin.board.controller.dto.CreateBoardResponseDto
import gunb0s.toy.ciazzakotlin.board.service.BoardService
import gunb0s.toy.ciazzakotlin.common.dto.ResponseDto
import gunb0s.toy.ciazzakotlin.common.exception.ErrorResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "board", description = "the board API")
class BoardController(
    private val boardService: BoardService,
) {
    @Operation(summary = "Add board", description = "add a new board to lecture", tags = ["board"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Successful operation",
            useReturnTypeSchema = true
        )]
    )
    @PostMapping("/board")
    fun create(@RequestBody createBoardDto: @Valid CreateBoardDto): ResponseEntity<ResponseDto<CreateBoardResponseDto>> {
        val board = boardService.create(createBoardDto)
        val responseDto: ResponseDto<CreateBoardResponseDto> = ResponseDto.created(CreateBoardResponseDto(board.id!!))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body<ResponseDto<CreateBoardResponseDto>>(responseDto)
    }

    @Operation(summary = "get board detail", description = "get board by id", tags = ["board"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            useReturnTypeSchema = true
        ), ApiResponse(
            responseCode = "404",
            description = "Board not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )]
        )]
    )
    @GetMapping("/board/{boardId}")
    fun get(@PathVariable boardId: Long): ResponseEntity<ResponseDto<BoardDto>> {
        val board = boardService.get(boardId)
        val responseDto: ResponseDto<BoardDto> = ResponseDto.ok(BoardDto(board))
        return ResponseEntity
            .ok(responseDto)
    }
}