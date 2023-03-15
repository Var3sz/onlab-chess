package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Pawn(color: String, viewModel: BoardViewModel, x: Int, y: Int  ): Piece  {
    var hasMoved: Boolean = false

    override var pieceColor: String = color
    override var i: Int =x
    override var j: Int =y



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