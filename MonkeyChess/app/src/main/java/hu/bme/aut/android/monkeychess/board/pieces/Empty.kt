package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName

class Empty(): Piece {

    override val imageID: Int
        get() {
            TODO()
        }

    override val name: PieceName = PieceName.EMPTY
    override var position: Pair<Int, Int> = Pair(0,0)

    override fun step(i: Int, j: Int){
    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>> {
        TODO("Not yet implemented")
    }

    override var hasMoved: Boolean = false
    override var pieceColor: PieceColor = PieceColor.EMPTY
    override var i: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override var j: Int
        get() = TODO("Not yet implemented")
        set(value) {}


}