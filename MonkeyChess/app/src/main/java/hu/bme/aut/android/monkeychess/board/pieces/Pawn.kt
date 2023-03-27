package hu.bme.aut.android.monkeychess.board.pieces

import android.util.Log
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName


class Pawn(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,

    ) : Piece {

    override val position: Pair<Int, Int> = Pair(i,j)
    override val name: PieceName = PieceName.PAWN
    var hasMoved: Boolean = false

    override var imageID: Int = 0

    init {
        if(pieceColor == PieceColor.BLACK){
            imageID = R.drawable.black_pawn
        }
        if(pieceColor == PieceColor.WHITE){
            imageID = R.drawable.white_pawn
        }
    }
    override fun step(){

    }

    override fun getValidSteps(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(2) { mutableListOf<Pair<Int, Int>>() }

        if(pieceColor == PieceColor.BLACK){
            if(i==1){
                steps[0].add(Pair(i+2,j))
            }
            if(i < 8 ){
                steps[1].add(Pair(i+1,j))
            }

        }
        else{
            if(pieceColor == PieceColor.BLACK){
                if(i==6){
                    steps[0].add(Pair(i-2,j))
                }
                if(i >= 0){
                    steps[1].add(Pair(i-1,j))
                }

            }
        }
        return steps
    }
}