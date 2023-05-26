package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


data class Rook(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {

    override val name: PieceName = PieceName.ROOK
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
    override fun step(i: Int, j: Int){
        this.i = i
        this.j = j
        this.position = Pair(i,j)
        hasMoved = true
    }

    override fun flip() {
        i = 7 - i
        j = 7 - j
        this.position = Pair(i,j)

        if(side==Side.DOWN) {
            side = Side.UP
        }

        else{
            side=Side.DOWN
        }

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