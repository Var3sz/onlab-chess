package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName

interface  Piece {
    fun step()
    fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>
    var pieceColor: PieceColor
    val imageID: Int
    val name: PieceName
    val position: Pair<Int, Int>



    var i: Int
    var j: Int

}