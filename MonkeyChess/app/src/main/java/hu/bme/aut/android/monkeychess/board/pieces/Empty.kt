package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R

class Empty(): Piece {

    override val imageID: Int
        get() {
            TODO()
        }

    override val name: String = "empty"


    override fun step() {
        TODO("Not yet implemented")
    }

    override fun getValidSteps(): List<Pair<Int, Int>> {
        TODO("Not yet implemented")
    }

    override var pieceColor: String = "empty"
    override var i: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override var j: Int
        get() = TODO("Not yet implemented")
        set(value) {}


}