package hu.bme.aut.android.monkeychess.board


import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side


class Ai() {
    val originalDepth = 3
    var branch: Pair<Piece,Pair<Int,Int>> = Pair(Empty(-1,-1),Pair(-1,-1))
    var bestChoice: Pair<Piece,Pair<Int,Int>> = Pair(Empty(-1,-1),Pair(-1,-1))

    private val aiColor = PieceColor.BLACK


    fun getTheNextStep(board: BoardViewModel): Pair<Piece,Pair<Int,Int>> {
        val boardData = board.copyBoard()

        minimax(board,board.copyBoard(),originalDepth,true,-6000000,7000000);
        board.loadBoard(boardData)
        board.addPiece(Empty (bestChoice.first.i, bestChoice.first.j))
        board.step(bestChoice.first, bestChoice.second.first, bestChoice.second.first)
        return bestChoice
    }


    fun minimax(
        board: BoardViewModel,
        boardState: MutableList<Piece>,
        depth: Int,
        maximizing: Boolean,
        alpha: Int,
        beta: Int
    ): Int {
        board.loadBoard(boardState)

        var alpha = alpha
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE
        var steps: List<Pair<Int,Int>> = board.getStepsforColor(aiColor)

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
            for (j in 0 until pieces.size){
                steps = board.getAvailableSteps(pieces[j], runspec = false)
                Log.d("AI", "depth ${depth} pieceName: ${pieces[j].name} piece place: ${pieces[j].position}")
                for (i in 0 until steps.size) {
                    Log.d("AI", "depth ${depth} pieceName: ${pieces[j].name} piece place: ${steps[i]}")
                    if (depth == originalDepth) {
                        branch = Pair(pieces[j],Pair(steps.get(i).first,steps.get(i).second))
                    }
                    val tmp = board.copyBoard()
                    board.step(pieces[j], steps[i].first, steps[i].second, false)

                    val value = minimax(board, tmp, depth - 1, false, alpha, beta)
                    max = Math.max(value, max)
                    if (max == value && depth == originalDepth) {
                        bestChoice = branch
                    }
                    alpha = Math.max(alpha, value)
                    if (beta <= alpha) {
                        break
                    }
                }
            }
            return max
        }

        else {
            val pieces = board.getPiecesbyColor(aiColor.oppositeColor())

            for (j in 0 until pieces.size){
                steps = board.getAvailableSteps(pieces[j])
                for (i in 0 until steps.size) {
                    val tmp = board.copyBoard()
                    board.step(pieces[j], steps[i].first, steps[i].second,false)
                    //if(boardEvaluator(tmp)!=0) {System.out.println("depth: "+(depth-1)+ "chosen column: "+ i+" board value: " +boardEvaluator(tmp)+" max: "+maxEval+" min: "+ minEval); drawBoard(tmp);}
                    val value = minimax(board, tmp, depth - 1, true, alpha, beta)
                    min = Math.min(value, min)
                    if (min == value && depth == 0) {
                        bestChoice = branch
                    }
                    alpha = Math.min(beta, value)
                    if (beta <= alpha) break
                }
            }
            return min
        }
    }


    private fun boardEvaluator(board: BoardViewModel): Int {
        var value = 0
        board.getAllPieces().forEach(){
            if(it.pieceColor == aiColor){
                when(it.name){
                    PieceName.PAWN -> {value++}
                    PieceName.KNIGHT -> {value=+3}
                    PieceName.BISHOP -> {value=+3}
                    PieceName.ROOK -> {value=+5}
                    PieceName.QUEEN -> {value=+9}
                    else -> {

                    }
                }
            }
            else{
                when(it.name){
                    PieceName.PAWN -> {value--}
                    PieceName.KNIGHT -> {value=-3}
                    PieceName.BISHOP -> {value=-3}
                    PieceName.ROOK -> {value=-5}
                    PieceName.QUEEN -> {value=-9}
                    else -> {

                    }
                }
            }
        }
        return value
    }


    class PlayGorund()

}
