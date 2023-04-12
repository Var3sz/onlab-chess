package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

interface  Piece {
    fun step(i: Int, j: Int)
    fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>
    fun flip()
    var pieceColor: PieceColor
    val imageID: Int
    val name: PieceName
    var position: Pair<Int, Int>
    var hasMoved: Boolean
    var side: Side
    var i: Int
    var j: Int

}