package hu.bme.aut.android.monkeychess.board

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

import java.lang.Math.abs
import kotlin.concurrent.thread
import kotlin.random.Random



class BoardViewModel(val doAi: Boolean = false,val  aiColor: PieceColor):  ViewModel() {
    //var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()

    var clickedPiece = MutableLiveData<Piece>()
    var currentPlayer = MutableLiveData<PieceColor>()
    var blackSide = MutableLiveData<Pair<PieceColor, Side>>()
    var previousMove: Pair<Int, Int>? = null
    var chanceForEnPassant: Boolean = false
    var whiteExchange = MutableLiveData<Boolean>(false)
    var blackExchange = MutableLiveData<Boolean>(false)

    //FEN variables
    var whiteCastleQueenSide = true
    var whiteCastleKingSide = true
    var blackCastleQueenSide = true
    var blackCastleKingSide = true
    var numberOfRounds = 0

    var board = MutableLiveData<Board>()
    //var ai = Ai()

//////////////////////////////////////////////////////////////////////////////
//  Logic for finding the available steps
    init{
        board.value = Board("")
        currentPlayer.value = PieceColor.WHITE

        if(aiColor == PieceColor.WHITE && doAi){
            board.value?.doAiStep(aiColor)
            board.value?.ChangeCurrentPlayer()
            ChangeCurrentPlayer()
        }

    }
    fun getAvailableSteps(piece: Piece, color: PieceColor = currentPlayer.value!!, runspec: Boolean = true): MutableList<Pair<Int, Int>> {
        Log.d("COLOR", "view: ${currentPlayer.value} board: ${ board.value?.currentPlayerBoard}")
        board.value?.currentPlayerBoard = currentPlayer.value!!
        return board.value?.getAvailableSteps(piece, color,runspec ) ?: return mutableListOf<Pair<Int, Int>>()
    }


    //////////////////////////////////////////////////////////////////////////////
    //  Different steps and step logic
    fun step(piece: Piece, i: Int, j: Int, doai: Boolean = false) {
        board.value?.step(piece,i,j,doai)
        ChangeCurrentPlayer()
        if(doAi){
            Log.d("AI", doAi.toString())
            doAiStep()
        }

    }

    private fun doAiStep() {
        val th = thread {
            board.value?.doAiStep(aiColor)
        }
        board.value?.ChangeCurrentPlayer()
        ChangeCurrentPlayer()
    }


    fun ChangeCurrentPlayer() {
        var color = currentPlayer.value
        color = color?.oppositeColor()
        currentPlayer.value = color
    }

    //////////////////////////////////////////////////////////////////////////////
// getter for pieces or bord information
    fun getCurrentPlayer(): PieceColor {
        return currentPlayer.value ?: PieceColor.EMPTY
    }


    fun getValue(row: Int, col: Int): Boolean? {
        return board.value?.getValue(row,col)
    }

    fun getPiece(row: Int, col: Int): Piece {
        return board.value?.getPiece(Pair(row,col))!!
    }

    fun getClickedPiece(): Piece {
        return clickedPiece.value!!
    }


    //////////////////////////////////////////////////////////////////////////////
// setters for pieces or bord information
    fun setValue(row: Int, col: Int, value: Boolean) {
        board.value?.setValue(row,col,value)
    }

    fun setClickedPiece(piece: Piece?) {
        clickedPiece.value = piece
    }

    fun HideAvailableSteps() {
        board.value?.HideAvailableSteps()
    }

    /////////Getters and Setter
    fun setWhiteExchangeState(state: Boolean){
        this.whiteExchange.value = state
    }

    fun getWhiteExchangeState(): MutableLiveData<Boolean>{
        return board.value?.getWhiteExchangeState()!!
        //return whiteExchange
    }
    fun setBlackExchangeState(state: Boolean){
        this.blackExchange.value = state
    }

    fun getBlackExchangeState(): MutableLiveData<Boolean>{
        return board.value?.getBlackExchangeState()!!
    }

}






//////////////////////////////////////////////////////////////////////////////
