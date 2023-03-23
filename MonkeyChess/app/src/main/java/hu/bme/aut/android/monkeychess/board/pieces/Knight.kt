package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Knight(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,
) : Piece {

    override var imageID: Int = 0
    override val name: String = "Knight"
    var hasMoved: Boolean = false


    override fun step(){

    }
    init {
        if(pieceColor == "Black"){
            imageID = R.drawable.black_knight
        }
        if(pieceColor == "White"){
            imageID = R.drawable.white_knight
        }
    }

    override fun getValidSteps(): List<Pair<Int, Int>>{
        val steps = mutableListOf <Pair<Int, Int>>()

        if(i + 2 < 8 && j +1 < 8){
            steps.add(Pair(i + 2, j +1))
        }

        if(i + 2 < 8 && j - 1 >= 0){
            steps.add(Pair(i + 2, j - 1))
        }

        if(i + 1 < 8 && j + 2 < 8){
            steps.add(Pair(i + 1, j + 2))
        }

        if(i + 1 < 8 && j - 2 >= 0){
            steps.add(Pair(i + 1, j - 2))
        }

        if(i - 1 >= 0 && j - 2 >= 0){
            steps.add(Pair(i - 1, j - 2))
        }

        if(i - 1 >= 0 && j + 2 < 8){
            steps.add(Pair(i - 1,  j + 2))
        }

        if(i - 2 >= 0 && j +1 < 8){
            steps.add(Pair(i - 2, j +1))
        }

        if(i - 2 >= 0 && j - 1 >= 0){
            steps.add(Pair(i - 2, j - 1))
        }


        return steps
    }
}