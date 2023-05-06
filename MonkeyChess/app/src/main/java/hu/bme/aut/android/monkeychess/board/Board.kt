package hu.bme.aut.android.monkeychess.board

import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

class Board(){
    val board = mutableListOf<MutableList<Tile>>()
    var currentPlayer: PieceColor = PieceColor.EMPTY


    constructor(pieces: MutableList<Piece>, color: PieceColor) : this() {
        currentPlayer = color
        for (i in 0 until 8) {
            val rowList = mutableListOf<Tile>()
            for (j in 0 until 8) {
                rowList.add(Tile(false, Empty(i,j)))
            }
            board.add(rowList)
        }
        loadBoard(pieces)
    }
    constructor(fenBoard: String) : this() {
        //initial
        if(fenBoard == ""){
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



        fun getAvailableSteps(
            piece: Piece,
            color: PieceColor,
            runspec: Boolean = true
        ): MutableList<Pair<Int, Int>> {
            val final = mutableListOf<Pair<Int, Int>>()
            //if(piece.pieceColor == color) {
            //debug
            if (piece.pieceColor == color || piece.pieceColor == color.oppositeColor()) {

                getavalibleStepsInaLine(piece, final)
                //pawn movement

                if (piece.name == PieceName.PAWN) {
                    pawnMovement(piece, final)
                }

                //castling


                if (runspec == true) {
                    checkAvailableStepsforCheck(piece, piece.pieceColor, final)

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
        fun step(piece: Piece, step: Pair<Int, Int>, doai: Boolean = true) {
        if (piece.name == PieceName.KING && !piece.hasMoved) {
            CastlingStep(piece, step.first, step.second)
            //Log.d("CAST", "${piece.hasMoved}")
        }
        else {
            ChangePiece(piece, step.first, step.second)
        }
            ChangeCurrentPlayer()
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


    fun ChangePiece(piece: Piece, i: Int, j: Int) {
            addPiece(Empty(piece.i,piece.j))

            piece.step(i, j)

            addPiece(piece)
        }

        fun ChangeCurrentPlayer() {
            currentPlayer = currentPlayer.oppositeColor()
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
        return board.get(pos.first).get(pos.second).pice
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

        //////////////////////////////////////////////////////////////////////////////
// setters for pieces or bord information
        fun addPiece(piece: Piece) {
            var rowlist = board.get(piece.i)
            rowlist[piece.j] = Tile(false, piece)
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

    }
