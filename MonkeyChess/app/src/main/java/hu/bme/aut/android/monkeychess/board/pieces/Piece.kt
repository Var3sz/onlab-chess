package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R

interface  Piece {
    fun step()
    fun getValidSteps(): List<Pair<Int, Int>>
    var pieceColor: String
    val imageID: Int
    val name: String



    var i: Int
    var j: Int

}