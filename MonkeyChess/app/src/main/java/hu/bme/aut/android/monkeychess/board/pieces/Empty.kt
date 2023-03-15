package hu.bme.aut.android.monkeychess.board.pieces

class Empty(): Piece {
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