package hu.bme.aut.android.monkeychess.board

import hu.bme.aut.android.monkeychess.board.pieces.Piece
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side

data class Tile(var free: Boolean, var pice:Piece)