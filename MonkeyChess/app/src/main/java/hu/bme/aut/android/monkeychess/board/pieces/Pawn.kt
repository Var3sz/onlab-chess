package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Pawn(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,

    ) : Piece {

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

    override fun getValidSteps(): List<Pair<Int, Int>>{
        val steps = mutableListOf <Pair<Int, Int>>()
        if(pieceColor == "Black"){
            if(i==1){
                steps.add(Pair(i+2,j))
            }
            if(i < 8 ){
                steps.add(Pair(i+1,j))
            }

        }
        return steps
    }
}