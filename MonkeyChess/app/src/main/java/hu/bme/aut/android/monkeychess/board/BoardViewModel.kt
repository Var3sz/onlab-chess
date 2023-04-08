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
                if(i==2){
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

                else if((i== 4 && j== 3)) {
                    rowList.add(Tile(false, Queen(PieceColor.WHITE, i, j)))
                }

                else if((i== 2 && j== 6)){
                    rowList.add(Tile(false,Queen(PieceColor.WHITE, i, j)))
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
        return final
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
        var matrix = tilesLiveData.value
        // tilesLiveData.value = matrix

        var newRowList = matrix?.get(piece.i)
        newRowList?.set(piece.j, Tile(false,Empty()))
        newRowList?.let {
            matrix?.set(piece.i, it)

            tilesLiveData.value= matrix
        }

        matrix = tilesLiveData.value
        piece.i = i
        piece.j = j
        piece.position = Pair(i,j)

        newRowList = matrix?.get(i)
        newRowList?.set(j, Tile(false,piece))
        newRowList?.let {
            matrix?.set(i, it)

            tilesLiveData.value= matrix
        }
        clickedPiece.value = null
    }


}