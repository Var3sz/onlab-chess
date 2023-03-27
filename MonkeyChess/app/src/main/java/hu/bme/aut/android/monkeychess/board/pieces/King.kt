package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class King(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,

    ) : Piece {

    override var imageID: Int = 0
    override val name: String = "King"
    override val position: Pair<Int, Int> = Pair(i,j)

    var hasMoved: Boolean = false



    init {
        if(pieceColor == "Black"){
            imageID = R.drawable.black_king
        }
        if(pieceColor == "White"){
            imageID = R.drawable.white_king
        }
    }

    override fun step(){

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(1) { mutableListOf<Pair<Int, Int>>() }

        if(i + 1 < 8 ){
            steps[0].add(Pair(i + 1, j))

            if(j - 1 >= 0){
                steps[0].add(Pair(i + 1, j - 1))
                steps[0].add(Pair(i, j - 1))
            }

            if(j + 1 < 8 ){
                steps[0].add(Pair(i + 1, j + 1))
                steps[0].add(Pair(i, j + 1))
            }
        }

        if(i - 1 >= 0 ){
            steps[0].add(Pair(i - 1, j))
            if(j - 1 >= 0)
                steps[0].add(Pair(i - 1, j - 1))
            if(j + 1 < 8 )
                steps[0].add(Pair(i - 1, j + 1))
        }


        return steps
    }



}