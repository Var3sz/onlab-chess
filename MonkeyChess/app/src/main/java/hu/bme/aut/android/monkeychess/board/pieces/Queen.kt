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
    override val name: String = "Queen"
    var hasMoved: Boolean = false
    override val position: Pair<Int, Int> = Pair(i,j)


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

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(8) { mutableListOf<Pair<Int, Int>>() }


        for (counter in 0 until 8){
            //diagonal
            if(counter + i != i && counter + i < 8  && j+counter < 8 )
                steps[0].add(Pair(counter + i,j + counter))

            if(counter + i != i && counter + i < 8 && j-counter >= 0 )
                steps[1].add(Pair(counter + i ,j - counter))


            if(counter + i != i && j + counter < 8 && i-counter >= 0){
                steps[2].add(Pair(i-counter,j+counter))
            }


            if(counter + i != i && j - counter >= 0 && i - counter >= 0)
                steps[3].add(Pair(i - counter,j - counter))


            //straight
            if (i + counter < 8)
                steps[4].add(Pair(i + counter, j))

            if (i - counter >= 0)
                steps[5].add(Pair(i - counter, j))

            if (j + counter < 8)
                steps[6].add(Pair(i, j + counter))

            if (j - counter >= 0)
                steps[7].add(Pair(i, j - counter))


        }


        return steps
    }



}