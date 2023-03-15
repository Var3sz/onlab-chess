package hu.bme.aut.android.monkeychess.board

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoardViewModel:  ViewModel()  {
    /*
    var matrix = Array(8) { BooleanArray(8) }

    fun getValue(i: Int,j: Int ): Boolean{
        return matrix[i][j]
    }

    fun setValue(i: Int,j: Int, value: Boolean){
        matrix[i][j]=value
    }

     */


    var matrixLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Boolean>>>()

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
    }

    fun setValue(row: Int, col: Int, value: Boolean) {
        val matrix = matrixLiveData.value
        matrix?.get(row)?.let { rowList ->
            val newRowList = rowList
            newRowList[col] = value
            matrix[row] = newRowList
            matrixLiveData.value = matrix
        }
    }

    fun getValue(row: Int, col: Int): Boolean? {
        return matrixLiveData.value?.getOrNull(row)?.getOrNull(col)
    }


}