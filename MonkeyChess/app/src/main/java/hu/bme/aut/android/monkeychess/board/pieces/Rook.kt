package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName


class Rook(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
) : Piece {

    override val name: PieceName = PieceName.ROOK
    var hasMoved: Boolean = false
    override var position: Pair<Int, Int> = Pair(i,j)
    override var imageID: Int = 0

    init {
        if(pieceColor == PieceColor.BLACK){
            imageID = R.drawable.black_rook
        }
        if(pieceColor == PieceColor.WHITE){
            imageID = R.drawable.white_rook
        }
    }
    override fun step(){

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(4) { mutableListOf<Pair<Int, Int>>() }

        for (counter in 0 until 8) {
            if (i + counter < 8)
                steps[0].add(Pair(i + counter, j))

            if (i - counter >= 0)
                steps[1].add(Pair(i - counter, j))

            if (j + counter < 8)
                steps[2].add(Pair(i, j + counter))

            if (j - counter >= 0)
                steps[3].add(Pair(i, j - counter))
        }
        return steps
    }
}