package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Knight(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,
    override val imageID: Int
) : Piece {

    override val name: String = "Knight"
    var hasMoved: Boolean = false


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