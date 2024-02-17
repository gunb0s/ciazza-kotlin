package gunb0s.toy.ciazzakotlin.board.repository

import gunb0s.toy.ciazzakotlin.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Long>