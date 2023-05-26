package hu.bme.aut.android.monkeychess.board

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

class Board(var aiBoard: Boolean= false){

    var blackCanCastleQueenSide: Boolean = false
    var blackCanCastleKingSide: Boolean = false
    var whiteCanCastleQueenSide: Boolean = false
    var whiteCanCastleKingSide: Boolean = false

    var board = mutableListOf<MutableList<Tile>>()
    var currentPlayerBoard: PieceColor = PieceColor.EMPTY
    var previousMove: Pair<Int, Int>? = null

    var chanceForEnPassant: Boolean = false
    var whiteExchange = MutableLiveData<Boolean>(false)
    var blackExchange = MutableLiveData<Boolean>(false)
    var whiteSide = Side.DOWN

    var playerOne: String? = null
    var playerTwo: String? = null
    var currentUser: String? = null


    constructor(pieces: MutableList<Piece>, color: PieceColor, aiBoard: Boolean = false) : this() {
        currentPlayerBoard = color
        for (i in 0 until 8) {
            val rowList = mutableListOf<Tile>()
            for (j in 0 until 8) {
                rowList.add(Tile(false, Empty(i,j)))
            }
            board.add(rowList)
            this.aiBoard = aiBoard
        }
        loadBoard(pieces)
    }

    constructor(fenBoard: String, _playerOne: String? = null, _playerTwo: String? = null, _currentUser: String? = null) : this() {
        playerOne = _playerOne
        playerTwo = _playerTwo
        currentUser = _currentUser
        Log.d("PlayerOneGeci: ", playerOne.toString())
        Log.d("PlayerTwoGeci: ", playerTwo.toString())
        Log.d("CurrentUserGeci: ", currentUser.toString())
        if (fenBoard.isNotBlank()) {
            val fenParts = fenBoard.split(" ")
            require(fenParts.size == 3) { "Invalid FEN string: $fenBoard" }

            val fenRows = fenParts[0].split("/")
            require(fenRows.size == 8) { "Invalid FEN string: $fenBoard" }

            val activeColor = fenParts[1]
            val isWhiteOnTop = fenBoard.endsWith("rnbqkbnr")

            val castlingRights = fenParts[2]
            val hasWhiteKingMoved = !castlingRights.contains("KQ")
            val hasBlackKingMoved = !castlingRights.contains("kq")
            val hasWhiteRookQueenSideMoved = !castlingRights.contains("Q")
            val hasWhiteRookKingSideMoved = !castlingRights.contains("K")
            val hasBlackRookQueenSideMoved = !castlingRights.contains("q")
            val hasBlackRookKingSideMoved = !castlingRights.contains("k")

            for (i in 0 until 8) {
                val fenRow = fenRows[i]
                val rowList = SnapshotStateList<Tile>()

                var j = 0
                for (char in fenRow) {
                    if (char.isDigit()) {
                        val emptyCount = char.toString().toInt()
                        for (k in 0 until emptyCount) {
                            rowList.add(Tile(false, Empty(i, j)))
                            j++
                        }
                    } else {
                        val pieceColor = if (char.isUpperCase()) PieceColor.WHITE else PieceColor.BLACK
                        val piece = getPieceFromFENChar(
                            char.toLowerCase(),
                            pieceColor,
                            i,
                            j,
                            isWhiteOnTop,
                            when {
                                pieceColor == PieceColor.WHITE && i == 7 && j == 4 -> hasWhiteKingMoved
                                pieceColor == PieceColor.WHITE && i == 7 && j == 0 -> hasWhiteRookQueenSideMoved
                                pieceColor == PieceColor.WHITE && i == 7 && j == 7 -> hasWhiteRookKingSideMoved
                                pieceColor == PieceColor.BLACK && i == 0 && j == 4 -> hasBlackKingMoved
                                pieceColor == PieceColor.BLACK && i == 0 && j == 0 -> hasBlackRookQueenSideMoved
                                pieceColor == PieceColor.BLACK && i == 0 && j == 7 -> hasBlackRookKingSideMoved
                                else -> true
                            }
                        )
                        rowList.add(Tile(false, piece))
                        j++
                    }
                }
                board.add(rowList)
            }
            currentPlayerBoard = if (activeColor == "w") PieceColor.WHITE else PieceColor.BLACK

            if (playerTwo == currentUser) {
                FlipTheTable()
            }
        }

        if(fenBoard == ""){
            currentPlayerBoard = PieceColor.WHITE
            val tiles = mutableListOf<MutableList<Tile>>()
            for (i in 0 until 8) {
                val rowList = SnapshotStateList<Tile>()

                for (j in 0 until 8) {
                    //setup board
                    //black Pawns
                    if (i == 1) {
                        rowList.add(Tile(false, Pawn(PieceColor.BLACK, i, j, Side.UP)))
                        //rowList.add(Tile(false,Empty()))
                    }
                    //black Rooks
                    else if ((i == 0 && j == 0) || (i == 0 && j == 7)) {
                        rowList.add(Tile(false, Rook(PieceColor.BLACK, i, j, Side.UP)))
                    }
                    //black Bishops

                    else if((i==0 && j==2)||(i==0 && j==5)){
                        rowList.add(Tile(false,Bishop(PieceColor.BLACK, i, j, Side.UP)))
                        //rowList.add(Tile(false,Empty()))
                    }
                    //black Knights
                    else if((i==0 && j==1)||(i==0 && j==6)){
                        rowList.add(Tile(false,Knight(PieceColor.BLACK, i, j, Side.UP)))
                        //rowList.add(Tile(false,Empty()))


                    } else if ((i == 0 && j == 3)) {
                        rowList.add(Tile(false, Queen(PieceColor.BLACK, i, j, Side.UP)))
                        //rowList.add(Tile(false,Empty()))
                    } else if ((i == 0 && j == 4)) {
                        rowList.add(Tile(false, King(PieceColor.BLACK, i, j, Side.UP)))
                        //rowList.add(Tile(false,Empty()))
                    }


                    //White Side

                    else if (i == 6) {
                        rowList.add(Tile(false, Pawn(PieceColor.WHITE, i, j, Side.DOWN)))
                        //rowList.add(Tile(false,Empty()))
                    }
                    //White Rooks
                    else if ((i == 7 && j == 0) || (i == 7 && j == 7)) {
                        rowList.add(Tile(false, Rook(PieceColor.WHITE, i, j, Side.DOWN)))
                    }
                    //White Bishops
                    else if((i==7 && j==2)||(i==7 && j==5)){
                        rowList.add(Tile(false,Bishop(PieceColor.WHITE, i, j, Side.DOWN)))
                        //rowList.add(Tile(false,Empty()))
                    }
                    //White Knights
                    else if((i==7 && j==1)||(i==7 && j==6)){
                        rowList.add(Tile(false,Knight(PieceColor.WHITE, i, j, Side.DOWN)))
                        //rowList.add(Tile(false,Empty()))
                    }

                    else if((i==7 && j==3)){
                        rowList.add(Tile(false,Queen(PieceColor.WHITE, i, j, Side.DOWN)))
                        //rowList.add(Tile(false,Empty()))
                    }
                    else if((i==7 && j==4)){
                        rowList.add(Tile(false,King(PieceColor.WHITE, i, j, Side.DOWN)))

                        //rowList.add(Tile(false,Empty()))
                    } else
                        rowList.add(Tile(false, Empty(i,j)))
                }
                board.add(rowList)
            }
        }
    }

    private fun parseCastlingRights(castlingRights: String) {
        resetCastlingRights()

        for (char in castlingRights) {
            when (char) {
                'K' -> whiteCanCastleKingSide = true
                'Q' -> whiteCanCastleQueenSide = true
                'k' -> blackCanCastleKingSide = true
                'q' -> blackCanCastleQueenSide = true
            }
        }
    }

    private fun resetCastlingRights() {
        whiteCanCastleKingSide = false
        whiteCanCastleQueenSide = false
        blackCanCastleKingSide = false
        blackCanCastleQueenSide = false
    }

    private fun getPieceFromFENChar(
        char: Char,
        color: PieceColor,
        row: Int,
        col: Int,
        isWhiteOnTop: Boolean,
        hasMoved: Boolean
    ): Piece {
        return when (char) {
            'p', 'P' -> {
                val side = if (color == PieceColor.WHITE) {
                    if (isWhiteOnTop) Side.UP else Side.DOWN
                } else {
                    if (isWhiteOnTop) Side.DOWN else Side.UP
                }
                Pawn(color, row, col, side)
            }
            'r', 'R' -> Rook(color, row, col, if (color == PieceColor.WHITE) Side.DOWN else Side.UP, hasMoved)
            'n', 'N' -> Knight(color, row, col, if (color == PieceColor.WHITE) Side.DOWN else Side.UP)
            'b', 'B' -> Bishop(color, row, col, if (color == PieceColor.WHITE) Side.DOWN else Side.UP)
            'q', 'Q' -> Queen(color, row, col, if (color == PieceColor.WHITE) Side.DOWN else Side.UP)
            'k', 'K' -> King(color, row, col, if (color == PieceColor.WHITE) Side.DOWN else Side.UP, hasMoved)
            else -> throw IllegalArgumentException("Invalid FEN character: $char")
        }
    }



        fun getAvailableSteps(
            piece: Piece,
            color: PieceColor,
            runspec: Boolean = true
        ): MutableList<Pair<Int, Int>> {
            val final = mutableListOf<Pair<Int, Int>>()

            if(piece.pieceColor == color) {
            //debug
            //if (piece.pieceColor == color || piece.pieceColor == color.oppositeColor()) {

                getavalibleStepsInaLine(piece, final)
                //pawn movement

                if (piece.name == PieceName.PAWN) {
                    pawnMovement(piece, final)
                }

                //castling


                if (runspec == true) {
                    checkAvailableStepsforCheck(piece, piece.pieceColor, final)
                    if(!aiBoard)
                    if (piece.name == PieceName.KING && !piece.hasMoved) {
                        GetValidCastling(piece, final)
                    }

                }

            }

            if (color == PieceColor.BLACK && currentUser != null && currentUser != playerTwo) {
                final.clear()
            }
            else if(color == PieceColor.WHITE && currentUser != null && currentUser != playerOne){
                final.clear()
            }

            return final
        }

        fun getavalibleStepsInaLine(piece: Piece, final: MutableList<Pair<Int, Int>>) {
            val valid = piece.getValidSteps()

            valid.forEach() {
                for (i in it.indices) {
                    val currentField = it[i]
                    val currentPiece = getPiece(currentField.first, currentField.second)

                    if (currentField != piece.position) {
                        if (currentPiece.name != PieceName.EMPTY) {
                            if (piece.pieceColor != currentPiece.pieceColor) {
                                final.add(currentField)
                            }
                            break
                        }
                        final.add(currentField)
                    }
                }
            }
        }

    fun checkAvailableStepsforCheck(
        piece: Piece,
        color: PieceColor,
        final: MutableList<Pair<Int, Int>>
    ) {
        val invalids = mutableListOf<Pair<Int, Int>>()
        val tmp = copyBoard()
        val origpos = Pair(piece.i, piece.j)
        val origmove = piece.hasMoved


        final.forEach() {
            ChangePiece(piece, it.first, it.second)
            if (noStepWhenChecked(piece.pieceColor)) {
                invalids.add(it)
            }
            ChangePiece(piece, origpos.first, origpos.second)
            piece.hasMoved = origmove
            tmp.forEach {

                addPiece(it)
            }

        }

        tmp.forEach {
            addPiece(it)
        }
        final.removeAll(invalids)
    }

    fun noStepWhenChecked(color: PieceColor): Boolean {
        val enemysteps = getStepsforColor(color.oppositeColor())
        enemysteps.forEach() {
            val tmp = getPiece(it.first, it.second)
            //Log.d("ez", "${it}")
            if (tmp.name == PieceName.KING && tmp.pieceColor == color) {
                return true
            }
        }
        return false
    }


    fun pawnMovement(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        var tmp: Piece
        val isUp = piece.side == Side.UP
        val sign = if (isUp) 1 else -1
        val i = piece.i + sign
        if (i in 0..7) {
            if (piece.j > 0) {
                tmp = getPiece(i, piece.j - 1)
                if (tmp.pieceColor != piece.pieceColor && tmp.pieceColor != PieceColor.EMPTY) {
                    final.add(Pair(i, piece.j - 1))
                }
            }
            if (piece.j < 7) {
                tmp = getPiece(i, piece.j + 1)
                if (tmp.pieceColor != piece.pieceColor && tmp.pieceColor != PieceColor.EMPTY) {
                    final.add(Pair(i, piece.j + 1))
                }
            }
            if (getPiece(i, piece.j).pieceColor != PieceColor.EMPTY) {
                final.remove(Pair(i, piece.j))
                final.remove(Pair(i + sign, piece.j))
            }
            if(i + sign in 1..6) {
                if (!piece.hasMoved && getPiece(i + sign, piece.j).pieceColor != PieceColor.EMPTY) {
                    final.add(Pair(i, piece.j))
                    final.remove(Pair(i + sign, piece.j))
                }
            }


            if(!aiBoard) {
                //Check for En Passant
                if (previousMove != null && piece.hasMoved && piece.i == (if (isUp) 4 else 3)) {

                    if (piece.j > 0) {
                        val left = getPiece(piece.i, piece.j - 1)
                        if (left.name == PieceName.PAWN && left.side != piece.side && left.hasMoved && left.i == previousMove?.first && left.j == previousMove?.second) {
                            final.add(Pair(i, piece.j - 1))
                            final.remove(Pair(left.i, left.j))
                            chanceForEnPassant = true
                        }
                    }

                    if (piece.j < 7) {
                        val right = getPiece(piece.i, piece.j + 1)
                        if (right.name == PieceName.PAWN && right.side != piece.side && right.hasMoved && right.i == previousMove?.first && right.j == previousMove?.second) {
                            final.add(Pair(i, piece.j + 1))
                            chanceForEnPassant = true
                        }
                    }
                }
            }
        }
    }

    fun GetValidCastling(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        if (piece.name != PieceName.KING || piece.hasMoved) {
            return
        }

        val kingRow = if (piece.side == Side.UP) 0 else 7
        val rookCandidates = listOf(
            Pair(kingRow, 0),
            Pair(kingRow, 7)
        )

        for (rookPos in rookCandidates) {
            val rook = getPiece(rookPos.first, rookPos.second)
            if (rook.name == PieceName.ROOK && !rook.hasMoved &&
                CheckGapForCastling(rook)) {
                var kingCol:Int

                if(whiteSide == Side.DOWN) {

                    kingCol = if (rookPos.second == 0) 2 else 6
                }
                else{
                    kingCol = if (rookPos.second == 0) 1 else 5

                }
                final.add(Pair(kingRow, kingCol))
            }
        }
    }

    fun CheckGapForCastling(rook: Piece): Boolean {
        val otherSteps = getStepsforColor(rook.pieceColor.oppositeColor())
        if (rook.name == PieceName.ROOK) {
            if (!rook.hasMoved) {
                //check if space is empty between king and rook
                var j = rook.j
                while (true) {
                    if (rook.j == 0) {
                        j++
                    }
                    if (rook.j == 7) {
                        j--
                    }
                    if (otherSteps.contains(Pair(rook.i, j))) {
                        return false
                    }

                    if (getPiece(rook.i, j).name == PieceName.KING) {
                        return true
                    }

                    if (getPiece(rook.i, j).name != PieceName.EMPTY) {
                        return false
                    }

                }
            }
        }
        return false
    }

    //////////////////////////////////////////////////////////////////////////////
    //  Different steps and step logic
    fun step(piece: Piece, i: Int, j: Int, doai: Boolean = true) {
        //Log.d("CURR", currentPlayer.value.toString())
        //king castling
        // Log.d("CAST ${piece.name}", "${piece.hasMoved}")
        if (piece.name == PieceName.KING && !piece.hasMoved) {
            CastlingStep(piece, i, j)
            //Log.d("CAST", "${piece.hasMoved}")
        }
        else if(piece.name == PieceName.PAWN && chanceForEnPassant){
            EnPassantStep(piece, i, j)
        }
        else if (piece.name == PieceName.PAWN && (i == 0 || i == 7) && piece.pieceColor == PieceColor.WHITE) {
            ChangePiece(piece, i, j)
            setWhiteExchangeState(true)
        }
        else if (piece.name == PieceName.PAWN && (i == 0 || i == 7) && piece.pieceColor == PieceColor.BLACK) {
            ChangePiece(piece, i, j)
            setBlackExchangeState(true)
        }
        //normal
        else {
            ChangePiece(piece, i, j)
        }

        previousMove = Pair(i, j)
        chanceForEnPassant = false

        //checkForCheck(piece.pieceColor)
        ChangeCurrentPlayer()
        //Log.d("FEN" ,printBoard())
        Log.d("FEN" , createFEN())
        var best = Pair<Piece, Pair<Int, Int>>(Empty(0,0), Pair(0,0))
        //Log.d("NEW BOARD", Board("").printBoard())
    }

     fun doAiStep(aiColor: PieceColor) {
         val boardForAi = Board(copyBoard(), currentPlayerBoard, true)
         val ai = Ai(boardForAi, aiColor)
         ai.board = boardForAi
         val nextStep = ai.getTheNextStep()

         if(getStepsforColor(aiColor).contains(nextStep.second)){
            StepforAI(nextStep)
         }else{
            Log.d("RAND" , "/n${printBoard()}/n ilegal step ${nextStep.second} piece ${nextStep.first.name} at pos: ${nextStep.first.position}" )
            rand()
             return
         }
    }
    fun rand(){
        val steps = mutableListOf<Pair<Piece, Pair<Int, Int>>>()
        getPiecesbyColor(PieceColor.BLACK).forEach(){
            val piece = it
            getAvailableSteps(piece, PieceColor.BLACK, true).forEach(){
                steps.add(Pair(piece, it))
            }
        }
        if(steps.isNotEmpty()){
            StepforAI(steps.random())

        }else{
            return
        }
    }

    fun StepforAI(step: Pair<Piece, Pair<Int, Int>>){
        val bestPiece = getPiece(step.first.i, step.first.j)
        step(bestPiece, step.second.first, step.second.second)
    }

    fun EnPassantStep(piece: Piece, i: Int, j: Int) {
        if (piece.name == PieceName.PAWN) {
            if (piece.j > 0) {
                val left = getPiece(piece.i, piece.j - 1)
                if (j == piece.j - 1 && left.name == PieceName.PAWN && left.side != piece.side && left.hasMoved && left.i == previousMove?.first && left.j == previousMove?.second) {
                    ChangePiece(piece, i, j)
                    addPiece(Empty(left.i, left.j))
                    return
                }
            }

            if (piece.j < 7) {
                val right = getPiece(piece.i, piece.j + 1)
                if (j == piece.j + 1 && right.name == PieceName.PAWN && right.side != piece.side && right.hasMoved && right.i == previousMove?.first && right.j == previousMove?.second) {
                    ChangePiece(piece, i, j)
                    addPiece(Empty(right.i, right.j))
                    return
                }
            }

            if (j == piece.j) {
                ChangePiece(piece, i, j)
            }
        }
    }

    fun CastlingStep(piece: Piece, i: Int, j: Int) {

        val kingRow = if (piece.side == Side.UP) 0 else 7
        val rookCandidates = listOf(
            getPiece(kingRow, 0),
            getPiece(kingRow, 7)
        )

        var rook : Piece = Empty(0,0)
        var rookJ = -1
        var castlingSides = Pair(6,2)
        if(whiteSide == Side.UP) castlingSides = Pair(5,1)

        if(j == castlingSides.first) {
            rook = rookCandidates[1]
            rookJ = castlingSides.first - 1
        }
        else if(j == castlingSides.second){
            rook = rookCandidates[0]
            rookJ = castlingSides.second + 1
        }
        if(rook.name == PieceName.ROOK && rookJ != -1){
            ChangePiece(rook, i, rookJ)

        }
        ChangePiece(piece, i, j)
    }


    fun ChangePiece(piece: Piece, i: Int, j: Int) {
            addPiece(Empty(piece.i,piece.j))

            piece.step(i, j)

            addPiece(piece)
        }

        fun ChangeCurrentPlayer() {


            currentPlayerBoard = currentPlayerBoard.oppositeColor()

        }

        //////////////////////////////////////////////////////////////////////////////
// getter for pieces or bord information
        fun getPiecesbyColor(color: PieceColor): List<Piece> {
            val fitingPieces = mutableListOf<Piece>()
            getAllPieces().forEach {
                if (it.pieceColor == color) {
                    fitingPieces.add(it)
                }
            }
            return fitingPieces
        }

        fun getAllPieces(): MutableList<Piece> {
            val listOfPieces = mutableListOf<Piece>()
            for (i in 0 until 8) {
                for (j in 0 until 8) {
                    val currPiece = getPiece(i, j)
                    if (currPiece.name != PieceName.EMPTY) {
                        listOfPieces.add(currPiece)
                    }
                }
            }
            return listOfPieces
        }


        fun getPiece(row: Int, col: Int): Piece {
            return board.get(row).get(col).pice
        }

        fun getPiece(pos: Pair<Int, Int>): Piece {
            val piece = board.get(pos.first).get(pos.second).pice
            return  piece
        }

        fun getStepsforColor(color: PieceColor, runspec: Boolean = false): List<Pair<Int, Int>> {
            val steps = mutableListOf<Pair<Int, Int>>()

            getPiecesbyColor(color).forEach() {
                steps.addAll(getAvailableSteps(it, color, runspec))
            }
            return steps
        }

    fun getStepsforColorWithPieces(color: PieceColor,runspec: Boolean = false ): List<Pair<Piece, Pair<Int,Int>>> {
        val steps = mutableListOf<Pair<Piece, Pair<Int,Int>>>()

        getPiecesbyColor(color).forEach() {
            val piece = it
            getAvailableSteps(piece, color, runspec).forEach(){
                steps.add(Pair(piece,it ))
            }
        }
        return steps
    }

    fun getValue(row: Int, col: Int): Boolean? {
        return board.getOrNull(row)?.getOrNull(col)?.free
    }



        //////////////////////////////////////////////////////////////////////////////
// setters for pieces or bord information
    fun addPiece(piece: Piece) {
        var rowlist = board.get(piece.i)
        rowlist[piece.j] = Tile(false, piece)
    }

    fun HideAvailableSteps() {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                setValue(i, j, false)
            }
        }
    }

    fun setValue(row: Int, col: Int, value: Boolean) {
        board.get(row).get(col).free = value
    }




    ///////////////////////////////////////
        //copy board
        fun copyBoard(): MutableList<Piece> {
            val copied = mutableListOf<Piece>()
            getAllPieces().forEach {
                when (it.name) {
                    PieceName.PAWN -> {
                        val copiedPiec = it as Pawn
                        copied.add(copiedPiec.copy())
                    }
                    PieceName.BISHOP -> {
                        val copiedPiec = it as Bishop
                        copied.add(copiedPiec.copy())
                    }
                    PieceName.KING -> {
                        val copiedPiec = it as King
                        copied.add(copiedPiec.copy())
                    }
                    PieceName.KNIGHT -> {
                        val copiedPiec = it as Knight
                        copied.add(copiedPiec.copy())
                    }
                    PieceName.QUEEN -> {
                        val copiedPiec = it as Queen
                        copied.add(copiedPiec.copy())
                    }
                    PieceName.ROOK -> {
                        val copiedPiec = it as Rook
                        copied.add(copiedPiec.copy())
                    }

                    else -> {}
                }
            }
            return copied
        }

        fun ClenaBoard(){
            for (i in 0 until 8){
                for (j in 0 until 8){
                    addPiece(Empty(i,j))
                }
            }
        }
        fun loadBoard(pieces: List<Piece>){
            ClenaBoard()
            pieces.forEach(){
                addPiece(it)
            }
        }

    fun printBoard(): String {
        var boarfen: String =""
        board.forEach{
            it.forEach(){
                var addedChar: Char ='e'

                when(it.pice.name){
                    PieceName.PAWN -> {
                        addedChar = 'p'
                    }
                    PieceName.KNIGHT -> {
                        addedChar = 'n'
                    }
                    PieceName.BISHOP -> {
                        addedChar = 'b'
                    }
                    PieceName.ROOK -> {
                        addedChar = 'r'
                    }
                    PieceName.QUEEN -> {
                        addedChar = 'q'
                    }
                    PieceName.KING -> {
                        addedChar = 'k'
                    }
                    else -> {}
                }


                when(it.pice.pieceColor){
                    PieceColor.BLACK->{
                        addedChar = addedChar.uppercase()[0]
                    }
                    PieceColor.WHITE->{}
                    PieceColor.EMPTY-> {
                        addedChar = '-'
                    }
                }
                boarfen += addedChar

            }
            boarfen += "\n"
        }
        return boarfen
    }

    fun createFEN(): String{
        var emptyTile = 0
        var boardFEN = ""

        val flipBoard = (this.whiteSide == Side.UP)

        if(flipBoard){
            board.reversed().forEach { row ->
                row.reversed().forEach { tile ->
                    var addedChar = ' '

                    when (tile.pice.name) {
                        PieceName.PAWN -> addedChar = 'p'
                        PieceName.KNIGHT -> addedChar = 'n'
                        PieceName.BISHOP -> addedChar = 'b'
                        PieceName.ROOK -> addedChar = 'r'
                        PieceName.QUEEN -> addedChar = 'q'
                        PieceName.KING -> addedChar = 'k'
                        else -> {}
                    }

                    when (tile.pice.pieceColor) {
                        PieceColor.WHITE -> addedChar = addedChar.uppercaseChar()
                        PieceColor.BLACK -> {}
                        PieceColor.EMPTY -> {
                            emptyTile++
                            return@forEach
                        }
                    }

                    if (emptyTile > 0) {
                        boardFEN += emptyTile.toString()
                        emptyTile = 0
                    }

                    boardFEN += addedChar
                }

                if (emptyTile > 0) {
                    boardFEN += emptyTile.toString()
                    emptyTile = 0
                }

                boardFEN += '/'
            }

            boardFEN = boardFEN.dropLast(1)

            if(currentPlayerBoard == PieceColor.WHITE){
                boardFEN+=" w "
            }
            else if(currentPlayerBoard == PieceColor.BLACK){
                boardFEN+=" b "
            }

            boardFEN += generateCastlingFen(this, true)

            return boardFEN
        }

        else{
            board.forEach { row ->
                row.forEach { tile ->
                    var addedChar = ' '

                    when (tile.pice.name) {
                        PieceName.PAWN -> addedChar = 'p'
                        PieceName.KNIGHT -> addedChar = 'n'
                        PieceName.BISHOP -> addedChar = 'b'
                        PieceName.ROOK -> addedChar = 'r'
                        PieceName.QUEEN -> addedChar = 'q'
                        PieceName.KING -> addedChar = 'k'
                        else -> {}
                    }

                    when (tile.pice.pieceColor) {
                        PieceColor.WHITE -> addedChar = addedChar.uppercaseChar()
                        PieceColor.BLACK -> {}
                        PieceColor.EMPTY -> {
                            emptyTile++
                            return@forEach
                        }
                    }

                    if (emptyTile > 0) {
                        boardFEN += emptyTile.toString()
                        emptyTile = 0
                    }

                    boardFEN += addedChar
                }

                if (emptyTile > 0) {
                    boardFEN += emptyTile.toString()
                    emptyTile = 0
                }

                boardFEN += '/'
            }

            boardFEN = boardFEN.dropLast(1)

            if(currentPlayerBoard == PieceColor.WHITE){
                boardFEN+=" w "
            }
            else if(currentPlayerBoard == PieceColor.BLACK){
                boardFEN+=" b "
            }

            boardFEN += generateCastlingFen(this, false)

            return boardFEN
        }
    }

    fun exchangePawn(pieceName: PieceName){
        var exchangeColor: PieceColor = PieceColor.EMPTY
        if(getWhiteExchangeState().value == true) {
            exchangeColor = PieceColor.WHITE
        }
        else if(getBlackExchangeState().value == true) {
            exchangeColor = PieceColor.BLACK
        }
        if(exchangeColor != PieceColor.EMPTY){
            if(pieceName == PieceName.QUEEN){
                ChangePiece(Queen(exchangeColor, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.ROOK){
                ChangePiece(Rook(exchangeColor, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.BISHOP){
                ChangePiece(Bishop(exchangeColor, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.KNIGHT){
                ChangePiece(Knight(exchangeColor, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
        }
        setWhiteExchangeState(state = false)
        setBlackExchangeState(state = false)
    }


    /////////Getters and Setter
    fun setWhiteExchangeState(state: Boolean){
        this.whiteExchange.value = state
    }

    fun getWhiteExchangeState(): MutableLiveData<Boolean>{
        return whiteExchange
    }
    fun setBlackExchangeState(state: Boolean){
        this.blackExchange.value = state
    }

    fun getBlackExchangeState(): MutableLiveData<Boolean>{
        return blackExchange
    }

    fun FlipTheTable() {
        val listOfPieces = getAllPieces()
        val tiles = board
        whiteSide = whiteSide.oppositeSide()

        listOfPieces.forEach() {
            it.flip()
        }


        for (i in 0 until 8) {
            val newRowList = tiles?.get(i)
            for (j in 0 until 8) {
                var add = false
                listOfPieces.forEach() {
                    if (it.position == Pair(i, j)) {
                        newRowList?.set(j, Tile(false, it))
                        //Log.d("FLIP", "name:${it.name} position:${it.position.toString()} color:${it.pieceColor} ")
                        add = true
                    }
                }
                if (!add)
                    newRowList?.set(j, Tile(false, Empty(i,j)))
            }
            newRowList?.let {
                tiles.set(i, it)

                board = tiles
            }
        }
    }

    fun generateCastlingFen(board: Board, isWhiteOnTop: Boolean): String {
        var castlingFen = ""

        if (isWhiteOnTop) {
            if (board.getPiece(0, 3) is King && board.getPiece(0, 3)?.pieceColor == PieceColor.WHITE) {
                if (board.getPiece(0, 0) is Rook && board.getPiece(0, 0)?.pieceColor == PieceColor.WHITE) {
                    castlingFen += "K"
                }
                if (board.getPiece(0, 7) is Rook && board.getPiece(0, 7)?.pieceColor == PieceColor.WHITE) {
                    castlingFen += "Q"
                }
            }

            if (board.getPiece(7, 3) is King && board.getPiece(7, 3)?.pieceColor == PieceColor.BLACK) {
                if (board.getPiece(7, 0) is Rook && board.getPiece(7, 0)?.pieceColor == PieceColor.BLACK) {
                    castlingFen += "k"
                }
                if (board.getPiece(7, 7) is Rook && board.getPiece(7, 7)?.pieceColor == PieceColor.BLACK) {
                    castlingFen += "q"
                }
            }
        } else {
            if (board.getPiece(7, 4) is King && board.getPiece(7, 4)?.pieceColor == PieceColor.WHITE) {
                if (board.getPiece(7, 7) is Rook && board.getPiece(7, 7)?.pieceColor == PieceColor.WHITE) {
                    castlingFen += "K"
                }
                if (board.getPiece(7, 0) is Rook && board.getPiece(7, 0)?.pieceColor == PieceColor.WHITE) {
                    castlingFen += "Q"
                }
            }

            if (board.getPiece(0, 4) is King && board.getPiece(0, 4)?.pieceColor == PieceColor.BLACK) {
                if (board.getPiece(0, 7) is Rook && board.getPiece(0, 7)?.pieceColor == PieceColor.BLACK) {
                    castlingFen += "k"
                }
                if (board.getPiece(0, 0) is Rook && board.getPiece(0, 0)?.pieceColor == PieceColor.BLACK) {
                    castlingFen += "q"
                }
            }
        }

        return if (castlingFen.isEmpty()) "-" else castlingFen
    }
}
