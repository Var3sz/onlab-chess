package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Pawn(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,

    ) : Piece {

    override val position: Pair<Int, Int> = Pair(i,j)
    override val name: String = "Pawn"
    var hasMoved: Boolean = false

    override var imageID: Int = 0

    init {
        if(pieceColor == "Black"){
            imageID = R.drawable.black_pawn
        }
        if(pieceColor == "White"){
            imageID = R.drawable.white_pawn
        }
    }
    override fun step(){

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(2) { mutableListOf<Pair<Int, Int>>() }

        if(pieceColor == "Black"){
            if(i==1){
                steps[0].add(Pair(i+2,j))
            }
            if(i < 8 ){
                steps[1].add(Pair(i+1,j))
            }

        }
        return steps
    }
}