package hu.bme.aut.android.monkeychess.board.pieces.enums


enum class PieceColor {
    BLACK, WHITE, EMPTY;

    fun oppositeColor(): PieceColor {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
            EMPTY -> EMPTY
        }
    }
}


