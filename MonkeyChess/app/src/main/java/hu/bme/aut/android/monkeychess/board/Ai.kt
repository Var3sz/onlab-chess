package hu.bme.aut.android.monkeychess.board


import android.util.Log
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

val pawnValue = 100
val bishopValue = 300
val knightValue = 300
val rookValue = 500
val queenValue = 900

fun bishopPositionValue( pair: Pair<Int, Int>): Int {
    var bishoppos = arrayOf(
        intArrayOf(-5, -5, -5, -5, -5, -5, -5, -5),
        intArrayOf(-5, 10, 5, 8, 8, 5, 10, -5),
        intArrayOf(-5, 5, 3, 8, 8, 3, 5, -5),
        intArrayOf(-5, 3, 10, 3, 3, 10, 3, -5),
        intArrayOf(-5, 3, 10, 3, 3, 10, 3, -5),
        intArrayOf(-5, 5, 3, 8, 8, 3, 5, -5),
        intArrayOf(-5, 10, 5, 8, 8, 5, 10, -5),
        intArrayOf(-5, -5, -5, -5, -5, -5, -5, -5)
    )
    return bishoppos[pair.first][pair.second]
}
fun knightPositionValue( pair: Pair<Int, Int>): Int {
    var knightpos = arrayOf(
    intArrayOf(-10, -5, -5, -5, -5, -5, -5, -10),
    intArrayOf(-8, 0, 0, 3, 3, 0, 0, -8),
    intArrayOf(-8, 0, 10, 8, 8, 10, 0, -8),
    intArrayOf(-8, 0, 8, 10, 10, 8, 0, -8),
    intArrayOf(-8, 0, 8, 10, 10, 8, 0, -8),
    intArrayOf(-8, 0, 10, 8, 8, 10, 0, -8),
    intArrayOf(-8, 0, 0, 3, 3, 0, 0, -8),
    intArrayOf(-10, -5, -5, -5, -5, -5, -5, -10)
    )
    return knightpos[pair.first][pair.second]
}

fun enenemyPawnPosition( pair: Pair<Int, Int>): Int {
    val pawnposEnemy = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(5, 10, 15, 20, 20, 15, 10, 5),
        intArrayOf(4, 8, 12, 16, 16, 12, 8, 4),
        intArrayOf(0, 6, 9, 10, 10, 9, 6, 0),
        intArrayOf(0, 4, 6, 10, 10, 6, 4, 0),
        intArrayOf(0, 2, 3, 4, 4, 3, 2, 0),
        intArrayOf(0, 0, 0, -5, -5, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    )
    return pawnposEnemy[pair.first][pair.second]
}

fun aiPawnPos( pair: Pair<Int, Int>): Int {
    val pawnposai = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, -5, -5, 0, 0, 0),
        intArrayOf(0, 2, 3, 4, 4, 3, 2, 0),
        intArrayOf(0, 4, 6, 10, 10, 6, 4, 0),
        intArrayOf(0, 6, 9, 10, 10, 9, 6, 0),
        intArrayOf(4, 8, 12, 16, 16, 12, 8, 4),
        intArrayOf(5, 10, 15, 20, 20, 15, 10, 5),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    )
    return pawnposai[pair.first][pair.second]
}

fun rookpose( pair: Pair<Int, Int>): Int {
    val rookpose = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(5, 10, 10, 10, 10, 10, 10, 5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(0, 0, 0, 5, 5, 0, 0, 0)
    )
    return rookpose[pair.first][pair.second]
}

fun queenpose( pair: Pair<Int, Int>): Int {
    val queenpose = arrayOf(
        intArrayOf(-20, -10, -10, -5, -5, -10, -10, -20),
        intArrayOf(-10, 0, 0, 0, 0, 0, 0, -10),
        intArrayOf(-10, 0, 5, 5, 5, 5, 0, -10),
        intArrayOf(-5, 0, 5, 5, 5, 5, 0, -5),
        intArrayOf(0, 0, 5, 5, 5, 5, 0, -5),
        intArrayOf(-10, 5, 5, 5, 5, 5, 0, -10),
        intArrayOf(-10, 0, 5, 0, 0, 0, 0, -10),
        intArrayOf(-20, -10, -10, -5, -5, -10, -10, -20)
    )
    return queenpose[pair.first][pair.second]
}

fun kingpose( pair: Pair<Int, Int>): Int {
    val kingpose =  arrayOf(
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-20, -30, -30, -40, -40, -30, -30, -20),
    intArrayOf(-10, -20, -20, -20, -20, -20, -20, -10),
    intArrayOf( 20,  20,   0,   0,   0,   0,  20,  20),
    intArrayOf( 20,  30,  10,   0,   0,  10,  30,  20)
    )
    return kingpose[pair.first][pair.second]
}
fun kingposeEnemy( pair: Pair<Int, Int>): Int {
    val kingpose =  arrayOf(
        intArrayOf( 20,  30,  10,   0,   0,  10,  30,  20),
        intArrayOf( 20,  20,   0,   0,   0,   0,  20,  20),
        intArrayOf(-10, -20, -20, -20, -20, -20, -20, -10),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30)
    )
    return kingpose[pair.first][pair.second]
}


fun getNumberOfSteps(board: Board, color: PieceColor): Int {
    return 5 * board.getStepsforColor(color).size
}




class Ai(val board: Board) {

    val originalDepth = 3
    var branch: Pair<Piece, Pair<Int, Int>> = Pair(Empty(-1, -1), Pair(-1, -1))
    var bestChoice: Pair<Piece, Pair<Int, Int>> = Pair(Empty(-1, -1), Pair(-1, -1))

    private val aiColor = PieceColor.BLACK


    fun getTheNextStep(): Pair<Piece, Pair<Int, Int>> {

        minimax(board, originalDepth, true, -6000000, 7000000);


        return bestChoice
    }

    fun getRandomStep(): Pair<Piece, Pair<Int, Int>> {
        Log.d("VAAAAAAAAAAAAAAAAAAA", board.printBoard())
        val steps= mutableListOf<Pair<Piece, Pair<Int, Int>>>()
        board.getPiecesbyColor(aiColor).forEach(){
            val piece= it
            board.getAvailableSteps(piece, aiColor, true).forEach {
                steps.add(Pair(piece, it))
            }
        }
        return steps[0]
    }



    fun minimax(
        board: Board,
        depth: Int,
        maximizing: Boolean,
        alpha: Int,
        beta: Int
    ): Int {


        var alpha = alpha
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE
        var steps: List<Pair<Int, Int>> = board.getStepsforColor(aiColor)

        if (depth == 0) return boardEvaluator(board)

        // ai loses
        if (steps.isEmpty()) {
            return -90000
        }

        // ai wins
        if (board.getStepsforColor(aiColor.oppositeColor()).isEmpty()) {
            return 50000
        }
        if (maximizing) {

            val steps = board.getStepsforColorWithPieces(aiColor, true)
            for (i in 0 until steps.size){
                //save current branch
                if(depth == originalDepth){
                    branch = steps[i]
                }
                //init new board
                val tmp = Board(board.copyBoard(), aiColor.oppositeColor())
                val tmpPiece = tmp.getPiece(steps[i].first.position)
                tmp.step(tmpPiece,steps[i].second)
               // Log.d("MAX", "\n${boardEvaluator(tmp)}\n${tmp.printBoard()}\n")
                //new board eval
                val value = minimax(tmp, depth-1, false, alpha, beta)
                max = Math.max(value, max)
                if( max == value && depth == originalDepth){
                    bestChoice = branch
                }

                alpha = Math.max(alpha, value)
                if(beta <= alpha){
                    break
                }

            }
            return max
        } else {
            //Log.d("MIN", "\n${board.printBoard()}\n")
            val steps = board.getStepsforColorWithPieces(aiColor.oppositeColor(), true)
            for (i in 0 until steps.size){
                //init new board
                val tmp = Board(board.copyBoard(), aiColor)
                val tmpPiece = tmp.getPiece(steps[i].first.position)
                tmp.step(tmpPiece, steps[i].second)

                //new board eval
                val value = minimax(tmp, depth-1, true, alpha, beta)
                min=Math.min(value, min)
                alpha=Math.min(beta, value);
                if(beta <= alpha){
                    break
                }
            }
        }
        return min
    }


    private fun boardEvaluator(board: Board): Int {
        var value = 0
        board.getAllPieces().forEach() {
            if (it.pieceColor == aiColor) {
                when (it.name) {
                    PieceName.PAWN -> {
                        value += pawnValue + aiPawnPos(it.position)
                    }
                    PieceName.KNIGHT -> {
                        value += knightValue + knightPositionValue(it.position)
                    }
                    PieceName.BISHOP -> {
                        value += bishopValue + bishopPositionValue(it.position)
                    }
                    PieceName.ROOK -> {
                        value += rookValue + rookpose(it.position)
                    }
                    PieceName.QUEEN -> {
                        value += queenValue + queenpose(it.position)
                    }
                    PieceName.KING -> {
                        value += kingpose(it.position)
                    }
                    else -> {

                    }
                }
            } else {
                when (it.name) {
                    PieceName.PAWN -> {
                        value -= (pawnValue + aiPawnPos(Pair(7 - it.i,7 - it.j)))
                    }
                    PieceName.KNIGHT -> {
                        value -= (knightValue + knightPositionValue(it.position))
                    }
                    PieceName.BISHOP -> {
                        value -= (bishopValue + bishopPositionValue(it.position))
                    }
                    PieceName.ROOK -> {
                        value -= (rookValue + rookpose(it.position))
                    }
                    PieceName.QUEEN -> {
                        value -= (queenValue + queenpose(it.position))
                    }
                    PieceName.KING -> {
                        value -= kingpose(Pair(7 - it.i,7 - it.j))
                    }
                    else -> {

                    }
                }
            }
        }
        value += getNumberOfSteps(board, aiColor)
        value -= getNumberOfSteps(board, aiColor.oppositeColor())
        return value
    }

}

class Board(pieces: MutableList<Piece>,color: PieceColor){
    val board = mutableListOf<MutableList<Tile>>()
    var currentPlayer: PieceColor
        init{
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
                //Black
                if (piece.name == PieceName.PAWN && piece.side == Side.UP) {
                    pawnMovement(piece, final)
                }
                //White
                if (piece.name == PieceName.PAWN && piece.side == Side.DOWN) {
                    pawnMovement(piece, final)
                }

                if (runspec == true) {
                    checkAvailableStepsforCheck(piece, piece.pieceColor, final)
                    //castling
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
        //////////////////////////////////////////////////////////////////////////////
//  Different steps and step logic
        fun step(piece: Piece, step: Pair<Int, Int>, doai: Boolean = true) {

            ChangePiece(piece, step.first, step.second)

            ChangeCurrentPlayer()
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













