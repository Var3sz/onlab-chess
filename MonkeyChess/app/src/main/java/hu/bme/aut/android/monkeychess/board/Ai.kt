package hu.bme.aut.android.monkeychess.board


import android.util.Log
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

class Ai(var board: Board, color: PieceColor) {

    val originalDepth = 3
    var branch: Pair<Piece, Pair<Int, Int>> = Pair(Empty(-1, -1), Pair(-1, -1))
    var bestChoice: Pair<Piece, Pair<Int, Int>> = Pair(Empty(-1, -1), Pair(-1, -1))

    private val aiColor = color


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



    fun getNumberOfSteps(board: Board, color: PieceColor): Int {
        return 5 * board.getStepsforColor(color).size
    }


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

            val steps = board.getStepsforColorWithPieces(aiColor, true)
            for (i in 0 until steps.size){
                //save current branch
                if(depth == originalDepth){
                    branch = steps[i]
                }
                //init new board
                val tmp = Board(board.copyBoard(), aiColor.oppositeColor())
                val tmpPiece = tmp.getPiece(steps[i].first.position)
                tmp.step(tmpPiece,steps[i].second.first, steps[i].second.second)
                //Log.d("MAX", "\n${boardEvaluator(tmp)}\n${tmp.printBoard()}\n")
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
                tmp.step(tmpPiece, steps[i].second.first, steps[i].second.second)
                //Log.d("MIN", "\n${boardEvaluator(tmp)}\n${tmp.printBoard()}\n")
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