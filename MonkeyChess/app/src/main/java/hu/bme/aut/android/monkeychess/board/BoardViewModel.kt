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
import kotlin.random.Random



class BoardViewModel:  ViewModel() {
    var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()
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
    //var ai = Ai()

    //////////////////////////////////////////////////////////////////////////////
// init block
    init {
        blackSide.value = Pair(PieceColor.BLACK, Side.UP)
        val tiles = SnapshotStateList<SnapshotStateList<Tile>>()
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
            tiles.add(rowList)
        }
        tilesLiveData.value = tiles
        currentPlayer.value = PieceColor.WHITE
    }

//////////////////////////////////////////////////////////////////////////////
//  Logic for finding the available steps

    fun getAvailableSteps(
        piece: Piece,
        color: PieceColor = currentPlayer.value!!,
        runspec: Boolean = true
    ): MutableList<Pair<Int, Int>> {
        val final = mutableListOf<Pair<Int, Int>>()
        if(piece.pieceColor == color) {
        //debug
        //if (piece.pieceColor == color || piece.pieceColor == color.oppositeColor()) {

            getavalibleStepsInaLine(piece, final)
            //pawn movement
            //Black
            if (piece.name == PieceName.PAWN && piece.side == Side.UP) {
                pawnMovement(piece, final)
            }
            //White
            if (piece.name == PieceName.PAWN && piece.side == Side.DOWN) {
                pawnMovement(piece, final)
            }

            if (final.size == 0) {
                //Log.d("MATE", "MATE")
            }

            if (runspec) {
                checkAvailableStepsforCheck(piece, piece.pieceColor, final)
                //castling
                if (piece.name == PieceName.KING && !piece.hasMoved) {
                    GetValidCastling(piece, final)
                }
            }

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


    fun pawnMovement(piece: Piece, final: MutableList<Pair<Int, Int>>, ) {
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

            if(i + sign in 1..6){
                if(!piece.hasMoved && getPiece(i + sign, piece.j).pieceColor != PieceColor.EMPTY){
                    final.add(Pair(i, piece.j))
                    final.remove(Pair(i+sign, piece.j))
                }
            }

            //Check for En Passant
            if (previousMove != null && piece.hasMoved && piece.i == (if (isUp) 4 else 3)) {
                val left = getPiece(piece.i, piece.j - 1)
                if (left.name == PieceName.PAWN && left.side != piece.side && left.hasMoved && left.i == previousMove?.first && left.j == previousMove?.second) {
                    final.add(Pair(i, piece.j - 1))
                    final.remove(Pair(left.i, left.j))
                    chanceForEnPassant = true
                }
                val right = getPiece(piece.i, piece.j + 1)
                if (right.name == PieceName.PAWN && right.side != piece.side && right.hasMoved && right.i == previousMove?.first && right.j == previousMove?.second) {
                    final.add(Pair(i, piece.j + 1))
                    final.remove(Pair(left.i, left.j))
                    chanceForEnPassant = true

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
                val kingCol = if (rookPos.second == 0) 2 else 6
                final.add(Pair(kingRow, kingCol))
            }
        }
        return
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
    fun step(piece: Piece, i: Int, j: Int, doai: Boolean = false) {
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
        Log.d("FEN" ,printBoard())
        Log.d("FEN" , createFEN())
        var best = Pair<Piece, Pair<Int, Int>>(Empty(0,0), Pair(0,0))
        if(currentPlayer.value == PieceColor.BLACK && doai){

            val board: Board = Board(copyBoard(), currentPlayer.value!!)


            val ai = Ai(board)

            var th= Thread{
                //val step = ai.getTheNextStep()
               // Log.d("MINMAx" ,step.toString())
                //Log.d("minmax", "i = ${step.first.i} j = ${step.first.j}")
                //best = ai.getRandomStep()
            }
            th.start()
            th.priority = 9
            th.join()
            val meu = Board(copyBoard(), PieceColor.BLACK)
            loadBoard(meu.copyBoard())
            Log.d("1FEN1" ,meu.printBoard())
            val nextStep = ai.getTheNextStep()

            if(getStepsforColor(PieceColor.BLACK).contains(nextStep.second)){
                StepforAI(nextStep)
            }else{
                Log.d("RAND" , "/n${printBoard()}/n ilegal step ${nextStep.second} piece ${nextStep.first.name} at pos: ${nextStep.first.position}" )
                rand()
            }



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
        StepforAI(steps.random())

    }
    fun StepforAI(step: Pair<Piece, Pair<Int, Int>>){
        val bestPiece = getPiece(step.first.i, step.first.j)
        step(bestPiece, step.second.first, step.second.second)
    }

    fun ChangePiece(piece: Piece, i: Int, j: Int) {
        addPiece(Empty(piece.i,piece.j))

        piece.step(i, j)

        addPiece(piece)
    }



    fun EnPassantStep(piece: Piece, i: Int, j: Int){
        val left = getPiece(piece.i, piece.j - 1)
        if (left.name == PieceName.PAWN && left.side != piece.side && left.hasMoved && left.i == previousMove?.first && left.j == previousMove?.second) {
            ChangePiece(piece, i, j)
            addPiece(Empty(left.i, left.j))
        }
        val right = getPiece(piece.i, piece.j + 1)
        if (right.name == PieceName.PAWN && right.side != piece.side && right.hasMoved && right.i == previousMove?.first && right.j == previousMove?.second) {
            ChangePiece(piece, i, j)
            addPiece(Empty(right.i, right.j))
        }
    }

    fun CastlingStep(piece: Piece, i: Int, j: Int) {
        val kingRow = if (piece.side == Side.UP) 0 else 7
        val rookCandidates = listOf(
            getPiece(kingRow, 0),
            getPiece(kingRow, 7)
        )
        val rook : Piece
        val rookJ: Int
        if(j == 6) {
            rook = rookCandidates[1]
            rookJ = 6 - 1
            ChangePiece(rook, i, rookJ)
        }
        else if(j == 2){
            rook = rookCandidates[0]
            rookJ = 2 + 1
            ChangePiece(rook, i, rookJ)
        }
        ChangePiece(piece, i, j)
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

    fun getValue(row: Int, col: Int): Boolean? {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)?.free
    }

    fun getPiece(row: Int, col: Int): Piece {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)!!.pice
    }

    fun getClickedPiece(): Piece {
        return clickedPiece.value!!
    }

    fun getStepsforColor(color: PieceColor): List<Pair<Int, Int>> {
        val steps = mutableListOf<Pair<Int, Int>>()

        getPiecesbyColor(color).forEach() {
            steps.addAll(getAvailableSteps(it, color, false))
        }
        return steps
    }

    //////////////////////////////////////////////////////////////////////////////
// setters for pieces or bord information
    fun setValue(row: Int, col: Int, value: Boolean) {
        val matrix = tilesLiveData.value
        // tilesLiveData.value = matrix

        val newRowList = matrix?.get(row)
        newRowList?.set(col, Tile(value, newRowList[col].pice))
        newRowList?.let {
            matrix.set(row, it)
            tilesLiveData.value = matrix
        }
    }

    fun setClickedPiece(piece: Piece?) {
        clickedPiece.value = piece
    }

    fun HideAvailableSteps() {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                setValue(i, j, false)
            }
        }
    }

    fun addPiece(piece: Piece) {
        val matrix = tilesLiveData.value

        val newRowList = matrix?.get(piece.i)
        newRowList?.set(piece.j, Tile(false, piece))
        newRowList?.let {
            matrix.set(piece.i, it)

            tilesLiveData.value = matrix
        }
    }

    //////////////////////////////////////////////////////////////////////////////
// The logic for table fliping
    fun FlipTheTable() {
        val listOfPieces = getAllPieces()
        val tiles = tilesLiveData.value

        listOfPieces.forEach() {
            it.flip()
            //Log.d("FLIP", "name:${it.name} position:${it.position.toString()} color:${it.pieceColor} ")
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

                tilesLiveData.value = tiles
            }
        }
        var side = blackSide.value
        if (side?.second == Side.UP) {
            side = Pair(PieceColor.BLACK, Side.DOWN)
        } else {
            side = Pair(PieceColor.BLACK, Side.UP)
        }
        blackSide.value = side
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
        tilesLiveData.value?.forEach{
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
        //Adding piece placement to Fen
        var emptyTile = 0
        var boardFEN = ""
        tilesLiveData.value?.forEach{
            it.forEach {
                var addedChar = ' '
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
                    PieceColor.WHITE->{
                        addedChar = addedChar.uppercase()[0]
                    }
                    PieceColor.BLACK->{}
                    PieceColor.EMPTY-> {
                        //addedChar = '-'
                        emptyTile++
                        return@forEach
                    }
                }
                if (emptyTile > 0) {
                    boardFEN += emptyTile.toString()
                    emptyTile = 0
                }
                boardFEN += addedChar

                /*if(blackSide.value?.first == PieceColor.BLACK || blackSide.value?.second == Side.UP){
                    if (it.pice.name == PieceName.KING && it.pice.pieceColor == PieceColor.WHITE) {
                        if (it.pice.hasMoved) {
                            whiteCastleKingSide = false;
                            whiteCastleQueenSide = false;
                        }
                    } else if (it.pice.name == PieceName.ROOK && it.pice.pieceColor == PieceColor.WHITE) {
                        if (it.pice.position.first != 7 && it.pice.position.second != 0)  {
                            whiteCastleQueenSide = false
                        } else if (it.pice.position.first != 7 && it.pice.position.second != 7) {
                            whiteCastleKingSide = false;
                        }
                    }
                }
                else if(blackSide.value?.first == PieceColor.BLACK || blackSide.value?.second == Side.DOWN){
                    if (it.pice.name == PieceName.KING && it.pice.pieceColor == PieceColor.WHITE) {
                        if (it.pice.hasMoved) {
                            whiteCastleKingSide = false;
                            whiteCastleQueenSide = false;
                        }
                    } else if (it.pice.name == PieceName.ROOK && it.pice.pieceColor == PieceColor.WHITE) {
                        if (it.pice.position.first != 0 && it.pice.position.second != 0)  {
                            whiteCastleQueenSide = false
                        } else if (it.pice.position.first != 0 && it.pice.position.second != 7) {
                            whiteCastleKingSide = false;
                        }
                    }
                }*/

            }
            if (emptyTile > 0) {
                if (emptyTile > 1) {
                    boardFEN += emptyTile.toString()
                }
                emptyTile = 0
             }
            boardFEN += '/'
        }


        //Drop last '/'char
        boardFEN = boardFEN.dropLast(1)

        //adding the active color to FEN
        if(getCurrentPlayer() == PieceColor.WHITE){
            boardFEN+=" w"
        }
        else if(getCurrentPlayer() == PieceColor.BLACK){
            boardFEN+=" b"
        }

        boardFEN += " -"    // TODO: CASTLING
        boardFEN += " - "    // TODO: half move clock

        //Fullmove number
        if(getCurrentPlayer() == PieceColor.BLACK){
            numberOfRounds++
        }
        boardFEN += numberOfRounds.toString()

        return boardFEN
    }

    fun exchangePawn(pieceName: PieceName){
        if(getWhiteExchangeState().value == true){
            if(pieceName == PieceName.QUEEN){
                ChangePiece(Queen(PieceColor.WHITE, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.ROOK){
                ChangePiece(Rook(PieceColor.WHITE, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.BISHOP){
                ChangePiece(Bishop(PieceColor.WHITE, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.KNIGHT){
                ChangePiece(Knight(PieceColor.WHITE, previousMove!!.first, previousMove!!.second, Side.UP), previousMove!!.first, previousMove!!.second)

            }
        }
        else if(getBlackExchangeState().value == true){
            if(pieceName == PieceName.QUEEN){
                ChangePiece(Queen(PieceColor.BLACK, previousMove!!.first, previousMove!!.second, Side.DOWN), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.ROOK){
                ChangePiece(Rook(PieceColor.BLACK, previousMove!!.first, previousMove!!.second, Side.DOWN), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.BISHOP){
                ChangePiece(Bishop(PieceColor.BLACK, previousMove!!.first, previousMove!!.second, Side.DOWN), previousMove!!.first, previousMove!!.second)

            }
            else if(pieceName == PieceName.KNIGHT){
                ChangePiece(Knight(PieceColor.BLACK, previousMove!!.first, previousMove!!.second, Side.DOWN), previousMove!!.first, previousMove!!.second)

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

}






//////////////////////////////////////////////////////////////////////////////
