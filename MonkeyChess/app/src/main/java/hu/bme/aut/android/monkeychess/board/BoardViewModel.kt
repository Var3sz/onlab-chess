package hu.bme.aut.android.monkeychess.board

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName

class BoardViewModel:  ViewModel()  {
    var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()
    var clickedPiece = MutableLiveData<Piece>()

    init {
        val tiles = SnapshotStateList<SnapshotStateList<Tile>>()
        for (i in 0 until 8){
            val rowList = SnapshotStateList<Tile>()

            for (j in 0 until 8){
                //stepup board
                //black Pawns
                if(i==1){
                    rowList.add(Tile(false,Pawn(PieceColor.BLACK, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }
                //black Rooks
                else if((i==0 && j==0)||(i==0 && j==7)){
                    rowList.add(Tile(false,Rook(PieceColor.BLACK, i, j)))
                }
                //black Bishops
                else if((i==0 && j==1)||(i==0 && j==6)){
                    rowList.add(Tile(false,Bishop(PieceColor.BLACK, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }
                //black Knights
                else if((i==0 && j==2)||(i==0 && j==5)){
                    rowList.add(Tile(false,Knight(PieceColor.BLACK, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }

                else if((i==0 && j==3)){
                    rowList.add(Tile(false,Queen(PieceColor.BLACK, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }
                else if((i==0 && j==4)){
                    rowList.add(Tile(false,King(PieceColor.BLACK, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }


                //White Side

                else if(i==6){
                    rowList.add(Tile(false,Pawn(PieceColor.WHITE, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }
                //White Rooks
                else if((i==7 && j==0)||(i==7 && j==7)){
                    rowList.add(Tile(false,Rook(PieceColor.WHITE, i, j)))
                }
                //White Bishops
                else if((i==7 && j==1)||(i==7 && j==6)){
                    rowList.add(Tile(false,Bishop(PieceColor.WHITE, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }
                //White Knights
                else if((i==7 && j==2)||(i==7 && j==5)){
                    rowList.add(Tile(false,Knight(PieceColor.WHITE, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }

                else if((i==7 && j==4)){
                    rowList.add(Tile(false,Queen(PieceColor.WHITE, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }
                else if((i==7 && j==3)){
                    rowList.add(Tile(false,King(PieceColor.WHITE, i, j)))
                    //rowList.add(Tile(false,Empty()))
                }


                else
                rowList.add(Tile(false,Empty()))
            }
            tiles.add(rowList)
        }
        tilesLiveData.value = tiles

    }

    fun setValue(row: Int, col: Int, value: Boolean) {

        val matrix = tilesLiveData.value
       // tilesLiveData.value = matrix

        val newRowList = matrix?.get(row)
        newRowList?.set(col, Tile(value,newRowList[col].pice))
        newRowList?.let {
            matrix.set(row, it)
            tilesLiveData.value= matrix
        }

    }

    fun getAvilableSteps(piece: Piece): MutableList<Pair<Int, Int>> {
        val valid = piece.getValidSteps()
        val final = mutableListOf<Pair <Int,Int>>()

        valid.forEach(){
            for(i in it.indices){
                val currentField = it[i]
                val currentPiece = getPiece(currentField.first, currentField.second)
                if(i == 0){
                    Log.d(piece.name.toString(), "i:${currentField.first} j:${currentField.first}+ name: ${currentPiece?.name}")

                }


                if(currentField != piece.position){
                    if(currentPiece?.name != PieceName.EMPTY){
                        if(piece.pieceColor != currentPiece?.pieceColor){
                            final.add(currentField)
                        }
                        break
                    }
                    final.add(currentField)
                }

            }
        }

        //pawn movement
        //Black
        if(piece.name == PieceName.PAWN && piece.pieceColor == PieceColor.BLACK) {
            if (piece.i < 7 && piece.j < 7) {
                if (getPiece(piece.i + 1, piece.j + 1)?.pieceColor == PieceColor.WHITE) {
                    final.add(Pair(piece.i + 1, piece.j + 1))
                }
            }
            if (piece.i < 7 && piece.j > 0){
                if (getPiece(piece.i + 1, piece.j - 1)?.pieceColor == PieceColor.WHITE) {
                    final.add(Pair(piece.i + 1, piece.j - 1))
                }
            }
            if(piece.i < 7) {
                if (getPiece(piece.i + 1, piece.j)?.pieceColor != PieceColor.EMPTY) {
                    final.remove(Pair(piece.i + 1, piece.j))
                    final.remove(Pair(piece.i + 2, piece.j))
                }
            }
        }
        //White
        if(piece.name == PieceName.PAWN && piece.pieceColor == PieceColor.WHITE){
            if (piece.i > 0 && piece.j < 7) {
                if (getPiece(piece.i - 1, piece.j + 1)?.pieceColor == PieceColor.BLACK) {
                    final.add(Pair(piece.i - 1, piece.j + 1))
                }
            }
            if (piece.i > 0 && piece.j > 0) {
                if (getPiece(piece.i - 1, piece.j - 1)?.pieceColor == PieceColor.BLACK) {
                    final.add(Pair(piece.i - 1, piece.j - 1))
                }
            }
            if(piece.i > 0) {
                if (getPiece(piece.i - 1, piece.j)?.pieceColor != PieceColor.EMPTY) {
                    final.remove(Pair(piece.i - 1, piece.j))
                    final.remove(Pair(piece.i - 2, piece.j))
                }
            }
        }

        //casteling
        if(piece.name == PieceName.KING && !piece.hasMoved){
            var rook = getPiece(0,0) ?: Empty()
            if(piece.pieceColor == PieceColor.BLACK){
                if(CheckGapForCasteling(rook) && rook.name == PieceName.ROOK){
                    final.add(Pair(0,2))
                }
                rook = getPiece(0,7) ?: Empty()
                if(CheckGapForCasteling(rook) && rook.name == PieceName.ROOK){
                    final.add(Pair(0,6))
                }
            }
            else{
                rook = getPiece(7,0) ?: Empty()
                if(CheckGapForCasteling(rook) && rook.name == PieceName.ROOK){
                    final.add(Pair(7,1))
                }
                rook = getPiece(7,7) ?: Empty()
                if(CheckGapForCasteling(rook) && rook.name == PieceName.ROOK){
                    final.add(Pair(7,5))
                }
            }
        }
        return final
    }

    fun CheckGapForCasteling(rook: Piece): Boolean{
        if(rook.hasMoved == false){
            //check if space is empty between king and rook
            var j = rook.j
            while (true) {
                if(rook.j ==0){
                    j++
                }
                if(rook.j ==7){
                    j--
                }
                if(getPiece(rook.i,j)?.name == PieceName.KING){
                    return true
                }
                if(getPiece(rook.i,j)?.name != PieceName.EMPTY){
                    return false
                }
            }
        }
        return false
    }

    fun HideAvibleSteps(){
        for (i in 0 until 8){
            for (j in 0 until 8){
                setValue(i,j,false)
            }
        }
    }
    fun getValue(row: Int, col: Int): Boolean? {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)?.free
    }

    fun getPiece(row: Int, col: Int): Piece? {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)!!.pice
    }

    fun getClickedPiece(): Piece{
        return clickedPiece.value!!
    }

    fun setClickedPiece(piece:Piece?){
        clickedPiece.value = piece
    }

    fun emptyLiveDataMatrix(){

    }

    fun step(piece: Piece, i: Int,j: Int){

        //king casteling
        if(piece.name == PieceName.KING && !piece.hasMoved){
            val rook: Piece
           if(piece.pieceColor == PieceColor.BLACK){
               if(i == 0 && j == 2) {
                   //put king to new place
                   ChangePiece(piece, i, j)
                   //get rook
                   rook = getPiece(0,0)!!
                   ChangePiece(rook,i,j+1)
               }
               else if(i == 0 && j == 6) {
                   //put king to new place
                   ChangePiece(piece, i, j)
                   //get rook
                   rook = getPiece(0,7)!!
                   ChangePiece(rook,i,j-1)
               }
               else{
                   ChangePiece(piece, i, j)
               }
           }
           else if(piece.pieceColor == PieceColor.WHITE){
               if(i == 7 && j == 1) {
                   //put king to new place
                   ChangePiece(piece, i, j)
                   //get rook
                   rook = getPiece(7,0)!!
                   ChangePiece(rook,i,j+1)
               }
               else if(i == 7 && j == 5) {
                   //put king to new place
                   ChangePiece(piece, i, j)
                   //get rook
                   rook = getPiece(7,7)!!
                   ChangePiece(rook,i,j-1)
               }
               else{
                   ChangePiece(piece, i, j)
               }
           }

        }
       else{
        ChangePiece(piece, i, j)
       }
    }

    fun ChangePiece(piece: Piece, i: Int,j: Int){
        var matrix = tilesLiveData.value
        // tilesLiveData.value = matrix

        var newRowList = matrix?.get(piece.i)
        newRowList?.set(piece.j, Tile(false,Empty()))
        newRowList?.let {
            matrix?.set(piece.i, it)

            tilesLiveData.value= matrix
        }

        matrix = tilesLiveData.value

        piece.step(i,j)

        newRowList = matrix?.get(i)
        newRowList?.set(j, Tile(false,piece))
        newRowList?.let {
            matrix?.set(i, it)

            tilesLiveData.value= matrix
        }
        clickedPiece.value = null
    }
}