package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel


class Rook(
    override var pieceColor: String,
    override var i: Int,
    override var j: Int,
    override val imageID: Int
) : Piece {

    override val name: String = "Rook"
    var hasMoved: Boolean = false


    override fun step(){

    }

    override fun getValidSteps(): List<Pair<Int, Int>>{
        val steps = mutableListOf <Pair<Int, Int>>()
        for (col in 0 until 8){
            if(col!=i)
                steps.add(Pair(col,j))

        }

        for (row in 0 until 8){
            if(row!=j)
                steps.add(Pair(i,row))
        }


        return steps
    }
}