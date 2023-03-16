package hu.bme.aut.android.monkeychess.board

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.monkeychess.board.pieces.Empty
import hu.bme.aut.android.monkeychess.board.pieces.Pawn
import hu.bme.aut.android.monkeychess.board.pieces.Piece

class BoardViewModel:  ViewModel()  {
    var matrixLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Boolean>>>()
    var piecesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Piece>>>()
    var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()

    init {
        val tiles = SnapshotStateList<SnapshotStateList<Tile>>()
        for (i in 0 until 8){
            val rowList = SnapshotStateList<Tile>()

            for (j in 0 until 8){
                if(i==1)
                    rowList.add(Tile(false,Pawn("Black", this, i, j)))
                else
                rowList.add(Tile(false,Empty()))
            }
            tiles.add(rowList)
        }
        tilesLiveData.value = tiles

    }

    /*
    init {
        val matrix = SnapshotStateList<SnapshotStateList<Boolean>>()
        repeat(8) { row ->
            val rowList = SnapshotStateList<Boolean>()
            repeat(8) { col ->
                rowList.add(false)
            }
            matrix.add(rowList)
        }
        matrixLiveData.value = matrix

        val pieces = SnapshotStateList<SnapshotStateList<Piece>>()
        for (i in 0 until 8){
            val rowList = SnapshotStateList<Piece>()

            for (j in 0 until 8){
                if(i==1)
                    rowList.add(Pawn("Black", this, i, j))

                else
                    rowList.add(Empty())
            }
            pieces.add(rowList)
        }

        //pieces.get(1).add(Pawn("Black", this, 1, 1))
        piecesLiveData.value = pieces

    }



     */
    fun setValue(row: Int, col: Int, value: Boolean) {

        val matrix = tilesLiveData.value

       // val tile = Tile(true,Pawn("Black", this, row, col ))
        // matrix?.get(row)?.set(col,tile)

        tilesLiveData.value = matrix


        val newRowList = matrix?.get(row)
        newRowList?.set(col, Tile(value,newRowList[col].pice))
        newRowList?.let {
            matrix.set(row, it)
            tilesLiveData.value= matrix
        }





    }

 /*

    fun setValue(row: Int, col: Int, value: Boolean) {
        val matrix = matrixLiveData.value
        matrix?.get(row)?.let { rowList ->
            val newRowList = rowList
            newRowList[col] = value
            matrix[row] = newRowList
            matrixLiveData.value = matrix
        }
    }

  */

    fun getValue(row: Int, col: Int): Boolean? {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)?.free
    }

    fun getPiece(row: Int, col: Int): Piece? {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)!!.pice
    }

    fun emptyLiveDataMatrix(){

    }


}