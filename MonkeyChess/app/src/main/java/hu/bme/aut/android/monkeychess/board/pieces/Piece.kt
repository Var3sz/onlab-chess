package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R

interface  Piece {
    fun step()
    fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>
    var pieceColor: String
    val imageID: Int
    val name: String
    val position: Pair<Int, Int>



    var i: Int
    var j: Int

}