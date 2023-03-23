package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Queen(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,

    ) : Piece {

    override var imageID: Int = 0
    override val name: String = "Bishop"
    var hasMoved: Boolean = false



    init {
        if(pieceColor == "Black"){
            imageID = R.drawable.black_queen
        }
        if(pieceColor == "White"){
            imageID = R.drawable.white_queen
        }
    }

    override fun step(){

    }

    override fun getValidSteps(): List<Pair<Int, Int>>{
        val steps = mutableListOf <Pair<Int, Int>>()

        for (col in 0 until 8){
            if(col + i != i && col + i < 8  && j+col < 8 )
                steps.add(Pair(col + i,j + col))

            if(col + i != i && col + i < 8 && j-col >= 0 )
                steps.add(Pair(col + i ,j - col))


            if(col + i != i && j + col < 8 && i-col >= 0){
                steps.add(Pair(i-col,j+col))
            }


            if(col + i != i && j - col >= 0 && i - col >= 0)
                steps.add(Pair(i - col,j - col))

            if(col!=i)
                steps.add(Pair(col,j))


            if(col!=j)
                steps.add(Pair(i,col))


        }


        return steps
    }



}