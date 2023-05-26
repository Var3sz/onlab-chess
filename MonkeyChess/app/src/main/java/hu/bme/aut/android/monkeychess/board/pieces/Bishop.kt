package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.board.Tile
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


data class Bishop(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var side: Side,
    override var hasMoved: Boolean = false
) : Piece {


    override var imageID: Int = 0
    override val name: PieceName = PieceName.BISHOP
    override var position: Pair<Int, Int> = Pair(i,j)



    init {
        if(pieceColor == PieceColor.BLACK){
            imageID = R.drawable.black_bishop
        }
        if(pieceColor == PieceColor.WHITE){
            imageID = R.drawable.white_bishop
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

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>> {
        val steps = Array(4) { mutableListOf<Pair<Int, Int>>() }

            for (col in 0 until 8){

                if(col + i != i && col + i < 8  && j+col < 8 ){
                    steps[0].add(Pair(col + i,j + col))

                }


                if(col + i != i && col + i < 8 && j-col >= 0 ){
                    steps[1].add(Pair(col + i ,j - col))
                }



                if(col + i != i && j + col < 8 && i-col >= 0){
                    steps[2].add(Pair(i-col,j+col))
                }


                if(col + i != i && j - col >= 0 && i - col >= 0){
                    steps[3].add(Pair(i - col,j - col))

                }
            }

        return steps
    }



}