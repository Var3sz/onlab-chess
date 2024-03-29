package hu.bme.aut.android.monkeychess.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.monkeychess.board.multi.Multiplayer
import hu.bme.aut.android.monkeychess.board.multi.NullableLiveData
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.single.SinglePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardViewModel(private val singlePlayer: SinglePlayer? = null, private val multiplayer: Multiplayer? = null, val doAi: Boolean = false, val  aiColor: PieceColor):  ViewModel() {
    var clickedPiece = MutableLiveData<Piece>()
    var currentPlayer = MutableLiveData<PieceColor>()
    var whiteExchange = MutableLiveData(false)
    var blackExchange = MutableLiveData(false)

    /** Single-player user data **/
    private var _currentUser = MutableLiveData<String>(singlePlayer?.currentUserLiveData?.value)
    val currentUser: LiveData<String?> get() = _currentUser
    private var _currentUserProfilePicture = MutableLiveData<String>(singlePlayer?.imageUrlLiveData?.value)
    val currentUserProfilePicture: LiveData<String?> get() = _currentUserProfilePicture

    /** Check mate **/
    private var whiteDefeated = MutableLiveData(false)
    private var blackDefeated = MutableLiveData(false)

    /** Multi-player user and game data **/
    var fen = multiplayer?.fen

    private val _playerOne = MutableLiveData<String>()
    val playerOne: LiveData<String?> get() = _playerOne

    private val _playerTwo = MutableLiveData<String>()
    val playerTwo: LiveData<String?> get() = _playerTwo

    private val _playerOneImage: LiveData<String?> = multiplayer?.playerOneImageUrlLiveData ?: NullableLiveData()
    private val _playerTwoImage: LiveData<String?> = multiplayer?.playerTwoImageUrlLiveData ?: NullableLiveData()
    private val _multiplayerCurrentUser: LiveData<String?> = multiplayer?.currentUser ?: NullableLiveData()

    val playerOneImage: LiveData<String?> get() = _playerOneImage
    val playerTwoImage: LiveData<String?> get() = _playerTwoImage
    val multiplayerCurrentUser: LiveData<String?> get() = _multiplayerCurrentUser

    private val _gameID = MutableLiveData<String?>()
    val gameID: LiveData<String?> get() = _gameID

    var isMulti: Boolean = false

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> get() = _board

    //////////////////////////////////////////////////////////////////////////////
//  Logic for finding the available steps
    init{
        if(multiplayer != null){
            isMulti = true
        }
            if (multiplayer?.isNewGame == true) {
                multiplayer.createNewGame(fen!!) { id, pOne, pTwo ->
                    setGameId(id.toString())
                    setPlayerOne(pOne.toString())
                    setPlayerTwo(pTwo.toString())
                    viewModelScope.launch {
                        multiplayer.receiveMove(gameID) { receivedFen ->
                            if (receivedFen != null) {
                                fen = receivedFen
                                val updatedBoard = Board(fen.toString(), playerOne.value.toString(), playerTwo.value.toString(), multiplayerCurrentUser.value.toString())
                                val color = receivedFen.split(" ").get(1)
                                updateBoard(updatedBoard, color)
                                Log.d("Received FEN: ", fen.toString())
                            } else {

                            }
                        }
                    }
                }
            }
            else if(multiplayer?.isNewGame == false){
                setGameId(multiplayer.gameId)
                setPlayerOne(multiplayer.playerOne)
                setPlayerTwo(multiplayer.playerTwo)
            }

            viewModelScope.launch {
                multiplayer?.receiveMove(gameID) { receivedFen ->
                    if (receivedFen != null) {
                        fen = receivedFen
                        val updatedBoard = Board(fen.toString(), playerOne.value.toString(), playerTwo.value.toString(), multiplayerCurrentUser.value.toString())
                        val color = receivedFen.split(" ").get(1)
                        updateBoard(updatedBoard, color)
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
                _board.value = Board(fen!!, playerOne.value.toString(), playerTwo.value.toString(), multiplayerCurrentUser.value.toString())
                val fenParts = fen!!.split(" ")
                /*if(fenParts[1] == "w"){
                    currentPlayer.value = PieceColor.WHITE
                }
                else{
                    currentPlayer.value = PieceColor.BLACK
                }*/
            }

            if(aiColor == PieceColor.WHITE && doAi){
                board.value?.doAiStep(aiColor)
                board.value?.ChangeCurrentPlayer()
                ChangeCurrentPlayer()
            }
    }

    fun getAvailableSteps(
        piece: Piece,
        color: PieceColor = currentPlayer.value!!,
        runspec: Boolean = true
    ): MutableList<Pair<Int, Int>>? {
        Log.d("COLOR", "view: ${currentPlayer.value} board: ${board.value?.currentPlayerBoard}")
        board.value?.currentPlayerBoard = currentPlayer.value!!

        if(multiplayer != null){
            if(color == PieceColor.BLACK && board.value?.currentUser != playerTwo.value){
                return mutableListOf()
            }
            else if(color == PieceColor.WHITE && board.value?.currentUser != playerOne.value){
                return mutableListOf()
            }
            return  board.value?.getAvailableSteps(
                piece,
                color,
                runspec
            )
        }

        return board.value?.getAvailableSteps(piece, color,runspec ) ?: return mutableListOf<Pair<Int, Int>>()
    }


    //////////////////////////////////////////////////////////////////////////////
    //  Different steps and step logic
    fun step(piece: Piece, i: Int, j: Int, doai: Boolean = false) {
        Log.d("CURRENT PLAYER", board.value?.currentPlayerBoard.toString())
        Log.d("PLAYER ON VIEWMODEL", currentPlayer.value.toString())
        board.value?.step(piece,i,j,doai)
        ChangeCurrentPlayer()

        if(doAi && whiteDefeated.value == false && blackDefeated.value == false){
            Log.d("AI", doAi.toString())
            val myScope = CoroutineScope(Dispatchers.IO)
            myScope.launch {
                doAiStep()
            }
        }
        multiplayer?.sendMove(gameID.value.toString(), board.value!!.createFEN())
    }

    private suspend fun doAiStep() {
        board.value?.doAiStep(aiColor)
        ChangeCurrentPlayer(true)
    }


    fun ChangeCurrentPlayer(ai: Boolean = false) {
        val tmp = Board(board.value?.copyBoard()!!, currentPlayer.value!!)
        tmp.getStepsforColor(PieceColor.BLACK,true)

        if(tmp.getStepsforColor(currentPlayer.value!!.oppositeColor(), true).isEmpty() == true){
            Log.d("MATE", "${currentPlayer.value!!.oppositeColor()} is defeated by ${currentPlayer.value.toString()}")
            if(currentPlayer.value == PieceColor.BLACK){
                whiteDefeated.postValue(true)
            }
            else if(currentPlayer.value == PieceColor.WHITE){
                blackDefeated.postValue(true)
            }
        }
            var color = currentPlayer.value
            color = color?.oppositeColor()
            currentPlayer.postValue(color)
    }

    //////////////////////////////////////////////////////////////////////////////
// getter for pieces or bord information
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
// setters for pieces or board information
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
    fun getWhiteExchangeState(): MutableLiveData<Boolean>{
        return board.value?.getWhiteExchangeState()!!
    }

    fun getBlackExchangeState(): MutableLiveData<Boolean>{
        return board.value?.getBlackExchangeState()!!
    }

    fun setGameId(_gameID: String) {
        this._gameID.value = _gameID
    }

    fun setPlayerOne(_playerOne: String){
        this._playerOne.value = _playerOne
    }

    fun setPlayerTwo(_playerTwo: String){
        this._playerTwo.value = _playerTwo
    }

    fun updateBoard(newBoard: Board, color: String){
        _board.value = newBoard
        currentPlayer.value = if (color == "w") PieceColor.WHITE else PieceColor.BLACK

    }

    fun getBlackDefeated(): MutableLiveData<Boolean> {
        return blackDefeated
    }
    fun getWhiteDefeated(): MutableLiveData<Boolean> {
        return  whiteDefeated
    }
}
//////////////////////////////////////////////////////////////////////////////