package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


class Queen(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side
    ) : Piece {

    override var imageID: Int = 0
    override val name: PieceName = PieceName.QUEEN
    override var hasMoved: Boolean = false
    override var position: Pair<Int, Int> = Pair(i,j)


    init {
        if(pieceColor == PieceColor.BLACK){
            imageID = R.drawable.black_queen
        }
        if(pieceColor == PieceColor.WHITE){
            imageID = R.drawable.white_queen
        }
    }

    override fun step(i: Int, j: Int){
        this.i = i
        this.j = j
        this.position = Pair(i,j)
        hasMoved = true
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