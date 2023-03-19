package hu.bme.aut.android.monkeychess.board

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.pieces.*

class BoardViewModel:  ViewModel()  {
    var matrixLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Boolean>>>()
    var piecesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Piece>>>()
    var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()

    init {
        val tiles = SnapshotStateList<SnapshotStateList<Tile>>()
        for (i in 0 until 8){
            val rowList = SnapshotStateList<Tile>()

            for (j in 0 until 8){
                //stepup board
                //black Pawns
                if(i==1){
                    rowList.add(Tile(false,Pawn("Black", i, j,R.drawable.pawn)))
                    //rowList.add(Tile(false,Empty()))
                }
                //black Rooks
                else if((i==0 && j==0)||(i==0 && j==7)){
                    rowList.add(Tile(false,Rook("Black", i, j,R.drawable.pawn)))
                }
                //black Bishops
                else if((i==0 && j==1)||(i==0 && j==6)){
                   // rowList.add(Tile(false,Bishop("Black", i, j,R.drawable.pawn)))
                    rowList.add(Tile(false,Empty()))
                }
                //black Knights
                else if((i==0 && j==2)||(i==0 && j==5)){
                    //rowList.add(Tile(false,Knight("Black", i, j,R.drawable.pawn)))
                    rowList.add(Tile(false,Empty()))
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
        tilesLiveData.value = matrix

        val newRowList = matrix?.get(row)
        newRowList?.set(col, Tile(value,newRowList[col].pice))
        newRowList?.let {
            matrix.set(row, it)
            tilesLiveData.value= matrix
        }





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

    fun emptyLiveDataMatrix(){

    }


}