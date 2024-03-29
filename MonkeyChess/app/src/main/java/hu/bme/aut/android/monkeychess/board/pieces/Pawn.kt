package hu.bme.aut.android.monkeychess.board.pieces

import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


data class Pawn(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {

    override var position: Pair<Int, Int> = Pair(i,j)
    override val name: PieceName = PieceName.PAWN

    override var imageID: Int = 0

    init {
        if(pieceColor == PieceColor.BLACK){
            imageID = R.drawable.black_pawn
        }
        if(pieceColor == PieceColor.WHITE){
            imageID = R.drawable.white_pawn
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
        val steps = Array(1) { mutableListOf<Pair<Int, Int>>() }

        if(side  == Side.UP){
            if(i==1){
                steps[0].add(Pair(i+2,j))
            }
            if(i < 7 ){
                steps[0].add(Pair(i+1,j))
            }

        }

        if(side  == Side.DOWN){
            if(i==6){
                steps[0].add(Pair(i-2,j))
            }
            if(i > 0){
                steps[0].add(Pair(i-1,j))
            }

        }

        return steps
    }
}