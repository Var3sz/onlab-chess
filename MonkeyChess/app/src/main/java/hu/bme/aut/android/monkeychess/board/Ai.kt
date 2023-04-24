package hu.bme.aut.android.monkeychess.board


import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.booleanResource
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


class Ai(val board: Board) {

    val originalDepth = 3
    var branch: Pair<Piece, Pair<Int, Int>> = Pair(Empty(-1, -1), Pair(-1, -1))
    var bestChoice: Pair<Piece, Pair<Int, Int>> = Pair(Empty(-1, -1), Pair(-1, -1))

    private val aiColor = PieceColor.BLACK


    fun getTheNextStep(): Pair<Piece, Pair<Int, Int>> {

        minimax(board, originalDepth, true, -6000000, 7000000);


        return bestChoice
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
            val pieces = board.getPiecesbyColor(aiColor)
            for (j in 0 until pieces.size) {
                steps = board.getAvailableSteps(pieces[j], aiColor,runspec = false)

                for (i in 0 until steps.size) {
                   // Log.d(
                    //    "AI",
                     //   "depth ${depth} pieceName: ${pieces[j].name} piece place: ${steps[i]}"
                    //)
                    if (depth == originalDepth) {
                        branch = Pair(pieces[j], Pair(steps.get(i).first, steps.get(i).second))
                    }
                    val tmp = Board(board.copyBoard(),aiColor.oppositeColor())
                    tmp.step(pieces[j], steps[i].first, steps[i].second, false)
                    Log.d("AI\n", "${tmp.printBoard()} i=${board.board.size} j= i=${board.board[3].size}")
                    val value = minimax(tmp ,depth - 1, false, alpha, beta)
                    max = Math.max(value, max)
                    if (max == value && depth == originalDepth) {
                        bestChoice = branch
                    }
                    alpha = Math.max(alpha, value)
                    if (beta <= alpha) {
                        return max
                    }
                }
            }
            return max
        } else {
            val pieces = board.getPiecesbyColor(aiColor.oppositeColor())

            for (j in 0 until pieces.size) {
                steps = board.getAvailableSteps(pieces[j], aiColor.oppositeColor(), false)
                for (i in 0 until steps.size) {
                    val tmp = Board(board.copyBoard(), aiColor)
                    tmp.step(pieces[j], steps[i].first, steps[i].second, false)

                    val value = minimax(tmp , depth - 1, true, alpha, beta)
                    min = Math.min(value, min)
                    if (min == value && depth == 0) {
                        bestChoice = branch
                    }
                    alpha = Math.min(beta, value)
                    if (beta <= alpha){
                        return min
                    }
                }
            }
            return min
        }
    }


    private fun boardEvaluator(board: Board): Int {
        var value = 0
        board.getAllPieces().forEach() {
            if (it.pieceColor == aiColor) {
                when (it.name) {
                    PieceName.PAWN -> {
                        value++
                    }
                    PieceName.KNIGHT -> {
                        value = +3
                    }
                    PieceName.BISHOP -> {
                        value = +3
                    }
                    PieceName.ROOK -> {
                        value = +5
                    }
                    PieceName.QUEEN -> {
                        value = +9
                    }
                    else -> {

                    }
                }
            } else {
                when (it.name) {
                    PieceName.PAWN -> {
                        value--
                    }
                    PieceName.KNIGHT -> {
                        value = -3
                    }
                    PieceName.BISHOP -> {
                        value = -3
                    }
                    PieceName.ROOK -> {
                        value = -5
                    }
                    PieceName.QUEEN -> {
                        value = -9
                    }
                    else -> {

                    }
                }
            }
        }
        return value
    }

}

class Board(pieces: MutableList<Piece>,color: PieceColor){
    val board = mutableListOf<MutableList<Tile>>()
    var currentPlayer: PieceColor
        init{
            for (i in 0 until 8){
                val rowlist = mutableListOf<Tile>()
                for (j in 0 until 8){
                    rowlist.add(Tile(false, Empty(i,j)))
                }
                board.add(rowlist)
            }
            currentPlayer = color

            //ClenaBoard()
            pieces.forEach(){addPiece(it)}
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
                if (!piece.hasMoved && getPiece(i + sign, piece.j).pieceColor != PieceColor.EMPTY) {
                    final.add(Pair(i, piece.j))
                    final.remove(Pair(i + sign, piece.j))
                }
            }
        }

        //////////////////////////////////////////////////////////////////////////////
//  Different steps and step logic
        fun step(piece: Piece, i: Int, j: Int, doai: Boolean = true) {

            ChangePiece(piece, i, j)

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

        fun getStepsforColor(color: PieceColor): List<Pair<Int, Int>> {
            val steps = mutableListOf<Pair<Int, Int>>()

            getPiecesbyColor(color).forEach() {
                steps.addAll(getAvailableSteps(it, color, false))
            }
            return steps
        }

        //////////////////////////////////////////////////////////////////////////////
// setters for pieces or bord information
        fun addPiece(piece: Piece) {
            board.get(piece.i).add(index = piece.j, Tile(false, piece))
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
        board?.forEach{
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
                        addedChar.uppercase()
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













