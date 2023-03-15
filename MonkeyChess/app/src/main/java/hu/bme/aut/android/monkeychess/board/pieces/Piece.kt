package hu.bme.aut.android.monkeychess.board.pieces

interface  Piece {
    fun step()
    fun getValidSteps(): List<Pair<Int, Int>>
    var pieceColor: String

    var i: Int
    var j: Int

}