package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


data class King(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {

    override var imageID: Int = 0
    override val name: PieceName = PieceName.KING
    override var position: Pair<Int, Int> = Pair(i,j)


    init {
        if(pieceColor == PieceColor.BLACK){
            imageID = R.drawable.black_king
        }
        if(pieceColor == PieceColor.WHITE){
            imageID = R.drawable.white_king
        }
    }

    override fun step(i: Int, j: Int){
        this.i = i
        this.j = j
        this.position = Pair(i,j)
        hasMoved = true
    }

    override fun flip() {
        i = 7 - i
        j = 7 - j
        this.position = Pair(i,j)

        if(side==Side.DOWN) {
            side = Side.UP
        }

        else{
            side=Side.DOWN
        }

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(8) { mutableListOf<Pair<Int, Int>>() }

        if(i + 1 < 8 ){
            steps[0].add(Pair(i + 1, j))

            if(j - 1 >= 0){
                steps[1].add(Pair(i + 1, j - 1))
            }

            if(j + 1 < 8 ){
                steps[2].add(Pair(i + 1, j + 1))
            }
        }

        if(j - 1 >= 0){
            steps[3].add(Pair(i, j - 1))
        }
        if(j + 1 < 8 ){
            steps[4].add(Pair(i, j + 1))
        }

        if(i - 1 >= 0 ){
            steps[5].add(Pair(i - 1, j))
            if(j - 1 >= 0)
                steps[6].add(Pair(i - 1, j - 1))
            if(j + 1 < 8 )
                steps[7].add(Pair(i - 1, j + 1))
        }
        return steps
    }
}