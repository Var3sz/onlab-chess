package hu.bme.aut.android.monkeychess.board

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.monkeychess.board.multi.Multiplayer
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side
import kotlinx.coroutines.launch

import kotlin.concurrent.thread

class BoardViewModel(val multiplayer: Multiplayer? = null, val doAi: Boolean = false, val  aiColor: PieceColor):  ViewModel() {
    //var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()
    var clickedPiece = MutableLiveData<Piece>()
    var currentPlayer = MutableLiveData<PieceColor>()
    var blackSide = MutableLiveData<Pair<PieceColor, Side>>()
    var previousMove: Pair<Int, Int>? = null
    var chanceForEnPassant: Boolean = false
    var whiteExchange = MutableLiveData<Boolean>(false)
    var blackExchange = MutableLiveData<Boolean>(false)

    var fen = multiplayer?.fen

    private val _gameID = MutableLiveData<String?>()
    val gameID: LiveData<String?> get() = _gameID


    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> get() = _board

    //var ai = Ai()


//////////////////////////////////////////////////////////////////////////////
//  Logic for finding the available steps
    init{
            if (multiplayer?.isNewGame == true) {
                multiplayer.createNewGame(fen!!) { id ->
                    setGameId(id.toString())
                    viewModelScope.launch {
                        multiplayer.receiveMove(gameID) { receivedFen ->
                            if (receivedFen != null) {
                                fen = receivedFen
                                val updatedBoard = Board(fen.toString())
                                updateBoard(updatedBoard)
                                Log.d("Received FEN: ", fen.toString())
                            } else {

                            }
                        }
                    }
                }
            }
            else if(multiplayer?.isNewGame == false){
                setGameId(multiplayer.gameId)
            }

            viewModelScope.launch {
                multiplayer?.receiveMove(gameID) { receivedFen ->
                    if (receivedFen != null) {
                        fen = receivedFen
                        val updatedBoard = Board(fen.toString())
                        updateBoard(updatedBoard)
                        Log.d("Received FEN: ", fen.toString())
                    } else {

                    }
                }
            }

            if(fen.isNullOrEmpty()){
                _board.value = Board("")
                currentPlayer.value = PieceColor.WHITE
            }
            else{
                _board.value = Board(fen!!)
                val fenParts = fen!!.split(" ")
                if(fenParts[1] == "w"){
                    currentPlayer.value = PieceColor.WHITE
                }
                else{
                    currentPlayer.value = PieceColor.BLACK
                }
            }

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
        multiplayer?.sendMove(gameID.value.toString(), board.value!!.createFEN())
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

    fun setGameId(_gameID: String) {
        this._gameID.value = _gameID
    }

    fun updateBoard(newBoard: Board){
        _board.value = newBoard
    }
}






//////////////////////////////////////////////////////////////////////////////
