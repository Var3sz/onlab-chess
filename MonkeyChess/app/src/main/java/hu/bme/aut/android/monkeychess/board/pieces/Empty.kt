package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

class Empty(
    override var i: Int,
    override var j: Int,
): Piece {

    override val imageID: Int
        get() {
            TODO()
        }

    override val name: PieceName = PieceName.EMPTY
    override var position: Pair<Int, Int> = Pair(0,0)
    override var side: Side
        get() = TODO("Not yet implemented")
        set(value) {}
    override fun step(i: Int, j: Int){
    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>> {
        TODO("Not yet implemented")
    }

    override var hasMoved: Boolean = false
    override var pieceColor: PieceColor = PieceColor.EMPTY




    override fun flip() {
    }


}