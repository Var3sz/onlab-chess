package hu.bme.aut.android.monkeychess.board

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.monkeychess.board.pieces.*
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

class BoardViewModel:  ViewModel()  {
    var tilesLiveData = MutableLiveData<SnapshotStateList<SnapshotStateList<Tile>>>()
    var clickedPiece = MutableLiveData<Piece>()
    var currentPlayer = MutableLiveData<PieceColor>()
    var blackSide = MutableLiveData <Pair<PieceColor, Side>> ()
    init {
        blackSide.value= Pair(PieceColor.BLACK, Side.UP)
        val tiles = SnapshotStateList<SnapshotStateList<Tile>>()
        for (i in 0 until 8){
            val rowList = SnapshotStateList<Tile>()

            for (j in 0 until 8){
                //setup board
                //black Pawns
                if(i==1){
                    rowList.add(Tile(false,Pawn(PieceColor.BLACK, i, j, Side.UP)))
                    //rowList.add(Tile(false,Empty()))
                }
                //black Rooks
                else if((i==0 && j==0)||(i==0 && j==7)){
                    rowList.add(Tile(false,Rook(PieceColor.BLACK, i, j, Side.UP)))
                }
                //black Bishops
                else if((i==0 && j==1)||(i==0 && j==6)){
                    rowList.add(Tile(false,Bishop(PieceColor.BLACK, i, j, Side.UP)))
                    //rowList.add(Tile(false,Empty()))
                }
                //black Knights
                else if((i==0 && j==2)||(i==0 && j==5)){
                    rowList.add(Tile(false,Knight(PieceColor.BLACK, i, j, Side.UP)))
                    //rowList.add(Tile(false,Empty()))
                }

                else if((i==0 && j==3)){
                    rowList.add(Tile(false,Queen(PieceColor.BLACK, i, j, Side.UP)))
                    //rowList.add(Tile(false,Empty()))
                }
                else if((i==0 && j==4)){
                    rowList.add(Tile(false,King(PieceColor.BLACK, i, j, Side.UP)))
                    //rowList.add(Tile(false,Empty()))
                }


                //White Side

                else if(i==6){
                    rowList.add(Tile(false,Pawn(PieceColor.WHITE, i, j, Side.DOWN)))
                    //rowList.add(Tile(false,Empty()))
                }
                //White Rooks
                else if((i==7 && j==0)||(i==7 && j==7)){
                    rowList.add(Tile(false,Rook(PieceColor.WHITE, i, j, Side.DOWN)))
                }
                //White Bishops
                else if((i==7 && j==1)||(i==7 && j==6)){
                    rowList.add(Tile(false,Bishop(PieceColor.WHITE, i, j, Side.DOWN)))
                    //rowList.add(Tile(false,Empty()))
                }
                //White Knights
                else if((i==7 && j==2)||(i==7 && j==5)){
                    rowList.add(Tile(false,Knight(PieceColor.WHITE, i, j, Side.DOWN)))
                    //rowList.add(Tile(false,Empty()))
                }

                else if((i==7 && j==4)){
                    rowList.add(Tile(false,Queen(PieceColor.WHITE, i, j, Side.DOWN)))
                    //rowList.add(Tile(false,Empty()))
                }
                else if((i==7 && j==3)){
                    rowList.add(Tile(false,King(PieceColor.WHITE, i, j, Side.DOWN)))
                    //rowList.add(Tile(false,Empty()))
                }


                else
                rowList.add(Tile(false,Empty()))
            }
            tiles.add(rowList)
        }
        tilesLiveData.value = tiles
        currentPlayer.value = PieceColor.WHITE
    }

    fun setValue(row: Int, col: Int, value: Boolean) {
        val matrix = tilesLiveData.value
       // tilesLiveData.value = matrix

        val newRowList = matrix?.get(row)
        newRowList?.set(col, Tile(value,newRowList[col].pice))
        newRowList?.let {
            matrix.set(row, it)
            tilesLiveData.value= matrix
        }
    }

    fun getAvailableSteps(piece: Piece, color: PieceColor = currentPlayer.value!!): MutableList<Pair<Int, Int>> {
        val final = mutableListOf<Pair <Int,Int>>()
        //if(piece.pieceColor == color) {
        //debug
        if(piece.pieceColor == color || piece.pieceColor == color.oppositeColor()){

            getavalibleStepsInaLine(piece,final)
            //pawn movement
            //Black
            if (piece.name == PieceName.PAWN && piece.side == Side.UP) {
                UpPawnMovement(piece, final)
            }
            //White
            if (piece.name == PieceName.PAWN && piece.side == Side.DOWN) {
                DownPawnMovement(piece, final)
            }

            //castling
            if (piece.name == PieceName.KING && !piece.hasMoved) {
                GetValidCastling(piece, final)
            }

            if(piece.name == PieceName.KING){
                BlockedByCheck(piece, final)
                Log.d("BLOCKed", "kingpos:${piece.position}, opositeavib:")
            }
        }
        return final
    }

    fun getAvailableHits(piece: Piece, color: PieceColor): MutableList<Pair<Int, Int>> {
        val final = mutableListOf<Pair <Int,Int>>()

        if(piece.pieceColor == color){
            getavalibleHitInaLine(piece,final)
            //pawn movement
            //Black
            if(piece.name == PieceName.PAWN){
                final.clear()
                if (piece.side == Side.UP) {
                    if(piece.i < 7){
                        if(piece.j < 7)
                            final.add(Pair(piece.i+1, piece.j+1))
                        if(piece.j > 0)
                            final.add(Pair(piece.i+1, piece.j-1))
                    }
                }
                //White
                if (piece.side == Side.DOWN) {
                    if(piece.i  > 0){
                        if(piece.j < 7)
                            final.add(Pair(piece.i-1, piece.j+1))
                        if(piece.j > 0)
                            final.add(Pair(piece.i-1, piece.j-1))
                    }
                }
            }
        }
        return final
    }

    fun getavalibleStepsInaLine(piece: Piece, final: MutableList<Pair<Int, Int>> ){
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

    fun getavalibleHitInaLine(piece: Piece, final: MutableList<Pair<Int, Int>> ){
        val valid = piece.getValidSteps()
        valid.forEach() {
            for (i in it.indices) {
                val currentField = it[i]
                val currentPiece = getPiece(currentField.first, currentField.second)

                if (currentField != piece.position) {
                    if (currentPiece.name != PieceName.EMPTY) {
                        final.add(currentField)
                        break
                    }
                    final.add(currentField)
                }
            }
        }
    }

    fun BlockedByCheck(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        val oppositSteps = getStepsforColor(piece.pieceColor.oppositeColor())
        oppositSteps.forEach(){
            Log.d("BLOCK", "kingpos:${piece.position}, opositeavi:${it}")
            if(final.contains(it)){
              final.remove(it)
            }
        }
    }

    fun GetValidCastling(piece: Piece, final: MutableList<Pair <Int,Int>>){
        if(piece.name == PieceName.KING && !piece.hasMoved) {
            var rook = getPiece(0, 0)
            if (piece.side == Side.UP) {
                if (CheckGapForCastling(rook)) {
                    final.add(Pair(0, 2))
                }
                rook = getPiece(0, 7)
                if (CheckGapForCastling(rook)) {
                    final.add(Pair(0, 6))
                }
            } else {
                rook = getPiece(7, 0)
                if (CheckGapForCastling(rook)) {
                    final.add(Pair(7, 1))
                }
                rook = getPiece(7, 7)
                if (CheckGapForCastling(rook)) {
                    final.add(Pair(7, 5))
                }
            }
        }
    }

    fun CheckGapForCastling(rook: Piece): Boolean{
        if(rook.name == PieceName.ROOK) {
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

    fun HideAvailableSteps(){
        for (i in 0 until 8){
            for (j in 0 until 8){
                setValue(i,j,false)
            }
        }
    }
    fun getValue(row: Int, col: Int): Boolean? {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)?.free
    }

    fun getPiece(row: Int, col: Int): Piece {
        return tilesLiveData.value?.getOrNull(row)?.getOrNull(col)!!.pice
    }

    fun getClickedPiece(): Piece{
        return clickedPiece.value!!
    }

    fun setClickedPiece(piece:Piece?){
        clickedPiece.value = piece
    }


    fun step(piece: Piece, i: Int,j: Int){
        Log.d("CURR", currentPlayer.value.toString())
        //king castling
        if(piece.name == PieceName.KING && !piece.hasMoved){
            CastlingStep(piece, i, j)
        }
        //normal
        else{
        ChangePiece(piece, i, j)
        }

        //checkForCheck()
        ChangeCurrentPlayer()
    }

    fun ChangePiece(piece: Piece, i: Int,j: Int){
        var matrix = tilesLiveData.value
        // tilesLiveData.value = matrix

        var newRowList = matrix?.get(piece.i)
        newRowList?.set(piece.j, Tile(false,Empty()))
        newRowList?.let {
            matrix?.set(piece.i, it)

            tilesLiveData.value= matrix
        }

        matrix = tilesLiveData.value

        piece.step(i,j)

        newRowList = matrix?.get(i)
        newRowList?.set(j, Tile(false,piece))
        newRowList?.let {
            matrix?.set(i, it)

            tilesLiveData.value= matrix
        }
        clickedPiece.value = null
    }

    fun CastlingStep(piece: Piece, i: Int,j: Int){
        val rook: Piece
        if(piece.side == Side.UP){
            if(i == 0 && j == 2) {
                //put king to new place
                ChangePiece(piece, i, j)
                //get rook
                rook = getPiece(0,0)
                ChangePiece(rook,i,j+1)
            }
            else if(i == 0 && j == 6) {
                //put king to new place
                ChangePiece(piece, i, j)
                //get rook
                rook = getPiece(0,7)
                ChangePiece(rook,i,j-1)
            }
            else{
                ChangePiece(piece, i, j)
            }
        }
        else if(piece.side == Side.DOWN){
            if(i == 7 && j == 1) {
                //put king to new place
                ChangePiece(piece, i, j)
                //get rook
                rook = getPiece(7,0)
                ChangePiece(rook,i,j+1)
            }
            else if(i == 7 && j == 5) {
                //put king to new place
                ChangePiece(piece, i, j)
                //get rook
                rook = getPiece(7,7)
                ChangePiece(rook,i,j-1)
            }
            else{
                ChangePiece(piece, i, j)
            }
        }
    }

    fun DownPawnMovement(piece: Piece, final: MutableList<Pair <Int,Int>>){
        var tmp: Piece
        if (piece.i > 0 && piece.j < 7) {
            tmp = getPiece(piece.i - 1, piece.j + 1)
            if (tmp.pieceColor != piece.pieceColor && tmp.pieceColor != PieceColor.EMPTY) {
                final.add(Pair(piece.i - 1, piece.j + 1))
            }
        }
        if (piece.i > 0 && piece.j > 0) {
            tmp = getPiece(piece.i - 1, piece.j - 1)
            if (tmp.pieceColor != piece.pieceColor && tmp.pieceColor != PieceColor.EMPTY) {
                final.add(Pair(piece.i - 1, piece.j - 1))
            }
        }
        if(piece.i > 0) {
            if (getPiece(piece.i - 1, piece.j).pieceColor != PieceColor.EMPTY) {
                final.remove(Pair(piece.i - 1, piece.j))
                final.remove(Pair(piece.i - 2, piece.j))
            }

            if(!piece.hasMoved){
                if (getPiece(piece.i - 2, piece.j).pieceColor != PieceColor.EMPTY) {
                    final.add(Pair(piece.i - 1, piece.j))
                    final.remove(Pair(piece.i - 2, piece.j))
                }
            }
        }
    }

    fun UpPawnMovement(piece: Piece, final: MutableList<Pair <Int,Int>>){
        var tmp: Piece
        if (piece.i < 7 && piece.j < 7) {
            tmp = getPiece(piece.i + 1, piece.j + 1)
            if (tmp.pieceColor != piece.pieceColor && tmp.pieceColor != PieceColor.EMPTY) {
                final.add(Pair(piece.i + 1, piece.j + 1))
            }
        }
        if (piece.i < 7 && piece.j > 0){
            tmp = getPiece(piece.i + 1, piece.j - 1)
            if (tmp.pieceColor != piece.pieceColor && tmp.pieceColor != PieceColor.EMPTY) {
                final.add(Pair(piece.i + 1, piece.j - 1))
            }
        }
        if(piece.i < 7) {
            if (getPiece(piece.i + 1, piece.j).pieceColor != PieceColor.EMPTY) {
                final.remove(Pair(piece.i + 1, piece.j))
                final.remove(Pair(piece.i + 2, piece.j))
            }
        }
        if(!piece.hasMoved) {
            if (getPiece(piece.i + 2, piece.j).pieceColor != PieceColor.EMPTY) {
                final.add(Pair(piece.i + 1, piece.j))
                final.remove(Pair(piece.i + 2, piece.j))
            }
        }
    }

    fun getAllPieces(): MutableList<Piece>{
        val listOfPieces = mutableListOf<Piece>()
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val currPiece = getPiece(i,j)
                if(currPiece.name != PieceName.EMPTY) {
                    listOfPieces.add(currPiece)
                }
            }
        }
        return  listOfPieces
    }
    fun FlipTheTable() {
        val listOfPieces = getAllPieces()
        val tiles = tilesLiveData.value

        listOfPieces.forEach(){
            it.flip()
            //Log.d("FLIP", "name:${it.name} position:${it.position.toString()} color:${it.pieceColor} ")
        }


        for (i in 0 until 8) {
            val newRowList = tiles?.get(i)
            for (j in 0 until 8) {
                var add = false
                listOfPieces.forEach() {
                    if (it.position == Pair(i, j)) {
                        newRowList?.set(j,Tile(false, it))
                        //Log.d("FLIP", "name:${it.name} position:${it.position.toString()} color:${it.pieceColor} ")
                        add = true
                    }
                }
                if (!add)
                    newRowList?.set(j,Tile(false, Empty()))
            }
            newRowList?.let {
                tiles.set(i, it)

                tilesLiveData.value= tiles
            }
        }
        var side = blackSide.value
        if(side?.second == Side.UP){
            side = Pair(PieceColor.BLACK, Side.DOWN)
        }
        else {
            side = Pair(PieceColor.BLACK, Side.UP)
        }
        blackSide.value = side
    }
    fun ChangeCurrentPlayer(){
        var color = currentPlayer.value
        color = color?.oppositeColor()
        currentPlayer.value = color
    }
    fun getStepsforColor(color: PieceColor = currentPlayer.value!!): List<Pair<Int,Int>>{
        val board = tilesLiveData.value
        val steps = mutableListOf<Pair<Int,Int>>()

        getAllPieces().forEach() {
            if(it.pieceColor == color){
                steps.addAll(getAvailableHits(it,color))
            }
        }
        return steps
    }

    fun checkForCheck(color: PieceColor = currentPlayer.value!!){
        val steps = getStepsforColor(color)
        var king : King

        getAllPieces().forEach {
            if(it.pieceColor == color.oppositeColor() && it.name == PieceName.KING){
                king = it as King
                Log.d("NOTCHECK", king.pieceColor.toString())
                if(steps.contains(king.position)){
                    Log.d("CHECK", king.toString())

                }
            }
        }
    }

    fun getCurrentPlayer(): PieceColor{
        return currentPlayer.value ?: PieceColor.EMPTY
    }


}